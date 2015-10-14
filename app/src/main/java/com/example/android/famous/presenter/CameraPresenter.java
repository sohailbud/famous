package com.example.android.famous.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.example.android.famous.model.Feed;
import com.example.android.famous.model.Location;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Sohail on 10/8/15.
 */

public class CameraPresenter {

    private static CameraPresenter cameraPresenter = null;

    public static CameraPresenter getInstance() {
        if (cameraPresenter == null) cameraPresenter = new CameraPresenter();
        return cameraPresenter;
    }

    public void saveParseObject(Uri mediaUri, Context context, Location location) {
        new SaveParseObject(mediaUri, context, location).execute();
    }

    private class SaveParseObject extends AsyncTask<Void, Void, Boolean> {

        Uri mediaUri;
        Context context;
        Location location;

        public SaveParseObject(Uri mediaUri, Context context, Location location) {
            this.mediaUri = mediaUri;
            this.context = context;
            this.location = location;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.i("RESULT", aBoolean.toString());
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return createNewParseFeed(mediaUri, context, location);
        }

        public boolean createNewParseFeed(Uri mediaUri, Context context, Location location) {

            Bitmap bitmap;
            ParseFile parseFile;
            ParseObject feedParse = new ParseObject("Feed");

            final ParseObject locationParse = new ParseObject("Location");
            locationParse.put("geoPoint", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));


            try {
                // throws file not found exception
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mediaUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                parseFile = new ParseFile(stream.toByteArray());

                try {
                    locationParse.save();
                    parseFile.save();

                    feedParse.put("Location", locationParse);
                    feedParse.put("User", ParseUser.getCurrentUser());
                    feedParse.put("Media", parseFile.getUrl());

                    feedParse.save();

                    return true;

                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace(); // file not found exception
                return false;
            }
        }
    }
}
