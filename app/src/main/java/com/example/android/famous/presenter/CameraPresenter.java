package com.example.android.famous.presenter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.example.android.famous.model.Location;
import com.example.android.famous.util.ImageResizer;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sohail on 10/8/15.
 */

public class CameraPresenter {

    private static CameraPresenter cameraPresenter = null;

    /**
     * if CameraPresenter instance exists, return that, otherwise create a new instance
     * @return CameraPresenter
     */
    public static CameraPresenter getInstance() {
        if (cameraPresenter == null) cameraPresenter = new CameraPresenter();
        return cameraPresenter;
    }

    /**
     * Executes AsyncTask to save image on parse
     * @param mediaUri - Uri to locate the media on phone
     * @param context - context of activity requesting this method
     * @param location - Location object
     */
    public void saveParseObject(Uri mediaUri, Context context, Location location) {
        new SaveParseObjectAsyncTask(mediaUri, context, location).execute();
    }

    private class SaveParseObjectAsyncTask extends AsyncTask<Void, Void, Boolean> {

        Uri mediaUri;
        Context context;
        Location location;

        public SaveParseObjectAsyncTask(Uri mediaUri, Context context, Location location) {
            this.mediaUri = mediaUri;
            this.context = context;
            this.location = location;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return createNewParseFeed(mediaUri, context, location);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        public boolean createNewParseFeed(Uri mediaUri, Context context, Location location) {
            Bitmap bitmap;
            ParseFile parseFile;
            ParseObject feedParse = new ParseObject("Feed");

            final ParseObject parseLocation = new ParseObject("Location");
            parseLocation.put("geoPoint", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));

            try {
//                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mediaUri);
                bitmap = ImageResizer.decodeSampledBitmapFromResource(getRealPathFromURI(context, mediaUri), 612, 612);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                parseFile = new ParseFile(stream.toByteArray());

                parseLocation.save();
                parseFile.save();

                feedParse.put("Location", parseLocation);
                feedParse.put("createdBy", ParseUser.getCurrentUser());
                feedParse.put("media", parseFile);

                feedParse.save();

                return true;

            } catch (ParseException e) {
                e.printStackTrace();
                return  false;
            }
        }

        public String getRealPathFromURI(Context context, Uri contentUri) {
            Cursor cursor = null;
            try {
                String[] proj = { MediaStore.Images.Media.DATA };
                cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }
}
