package com.example.android.famous.interactor;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.example.android.famous.model.Location;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

/**
 * Created by Sohail on 1/13/16.
 */
public class PostInteractor {

    private static PostInteractor postInteractor = null;

    private PostInteractor() {
    }

    public static PostInteractor getInstance() {
        if (postInteractor == null) postInteractor = new PostInteractor();
        return postInteractor;
    }

    public class PostObjectTask extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {
            Bitmap bitmap = (Bitmap) params[0];
            Location location = (Location) params[1];

            // Parse Location object
            ParseObject parseLocation = new ParseObject("Location");
            parseLocation.put("geoPoint", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));

            // Parse bitmap file
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            ParseFile parseFile = new ParseFile(stream.toByteArray());

            // Parse Feed object
            ParseObject parseFeed = new ParseObject("Feed");
            parseFeed.put("Location", parseLocation);
            parseFeed.put("createdBy", ParseUser.getCurrentUser());
            parseFeed.put("media", parseFile);

            try {
                parseFeed.save();
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }



}
