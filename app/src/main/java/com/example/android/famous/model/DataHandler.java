package com.example.android.famous.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.famous.model.DataContract.UserEntry;
import com.example.android.famous.model.DataContract.UserDetailsEntry;
import com.example.android.famous.model.DataContract.FeedEntry;
import com.example.android.famous.model.DataContract.LocationEntry;
import com.example.android.famous.model.DataContract.CommentEntry;
import com.example.android.famous.model.DataContract.LikesEntry;


/**
 * Created by Sohail on 10/30/15.
 */
public class DataHandler {

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase db;

    /**
     * creates an instance of the database
     * @param context
     */
    public DataHandler(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
    }

    /**
     * open the database
     * @return
     */
    public DataHandler open() {
        db = dataBaseHelper.getWritableDatabase();
        return this;
    }

    /**
     * close the database
     */
    public void close() {
        dataBaseHelper.close();
    }

    /**
     * checks if user already exists, return row id if it does else insert user data in user_entry table
     * @param user
     * @return
     */
    public long insertUserData(User user) {

        Cursor userCursor = returnUserData(user.getObjectId());

        if (userCursor.moveToNext()) {
            long row_ID = userCursor.getLong(userCursor.getColumnIndex(UserEntry._ID));
            userCursor.close();
            return row_ID;

        } else {
            ContentValues userValues = new ContentValues();
            userValues.put(UserEntry.COLUMN_NAME_OBJECT_ID, user.getObjectId());
            userValues.put(UserEntry.COLUMN_NAME_USERNAME, user.getUsername());
            userValues.put(UserEntry.COLUMN_NAME_FULL_NAME, user.getFullName());

            return db.insert(UserEntry.TABLE_NAME, null, userValues);
        }
    }

    /**
     * checks if location already exists, return row id if it does else insert location data in location_entry table
     * @param location
     * @return
     */
    public long insertLocationData(Location location) {

        Cursor locationCursor = returnLocationData(location.getObjectId());

        if (locationCursor.moveToNext()) {
            long row_ID = locationCursor.getLong(locationCursor.getColumnIndex(LocationEntry._ID));
            locationCursor.close();
            return row_ID;

        } else {
            ContentValues locationValues = new ContentValues();
            locationValues.put(LocationEntry.COLUMN_NAME_OBJECT_ID, location.getObjectId());
            locationValues.put(LocationEntry.COLUMN_NAME_LATITUDE, location.getLatitude());
            locationValues.put(LocationEntry.COLUMN_NAME_LONGITUDE, location.getLongitude());
            locationValues.put(LocationEntry.COLUMN_NAME_NAME, location.getName());

            return db.insert(LocationEntry.TABLE_NAME, null, locationValues);
        }
    }

    /**
     * checks if feed already exists, return row id if it does else insert feed data in feed_entry table
     * @param feed
     * @param user_ID
     * @param location_ID
     * @return
     */
    public long insertFeedData(Feed feed, long user_ID, long location_ID) {

        Cursor feedCursor = returnFeedData(feed.getObjectId());

        if (feedCursor.moveToNext()) {
            long row_ID = feedCursor.getLong(feedCursor.getColumnIndex(FeedEntry._ID));
            feedCursor.close();
            return row_ID;

        } else {
            ContentValues feedValues = new ContentValues();
            feedValues.put(FeedEntry.COLUMN_NAME_OBJECT_ID, feed.getObjectId());
            feedValues.put(FeedEntry.COLUMN_NAME_CREATED_AT, feed.getCreatedAt());
            feedValues.put(FeedEntry.COLUMN_NAME_LOCATION_KEY, location_ID);
            feedValues.put(FeedEntry.COLUMN_NAME_USER_KEY, user_ID);
            feedValues.put(FeedEntry.COLUMN_NAME_MEDIA_URI, feed.getMediaURI());
            feedValues.put(FeedEntry.COLUMN_NAME_TAGS, feed.getTags());

            return db.insert(FeedEntry.TABLE_NAME, null, feedValues);
        }
    }

    /**
     * FOLLOWING METHODS RUN QUERIES TO OBTAIN DATA FROM SQL DATABASE
     */

    public Cursor returnUserData(long user_ID) {
        final String SQL_RETURN_USER_DATA = "SELECT * FROM " + UserEntry.TABLE_NAME +
                " WHERE " + UserEntry._ID + " = ?";
        return db.rawQuery(SQL_RETURN_USER_DATA, new String[]{Long.toString(user_ID)});
    }

    public Cursor returnUserData(String parseObjectId) {
        final String SQL_RETURN_USER_DATA = "SELECT * FROM " + UserEntry.TABLE_NAME +
                " WHERE " + UserEntry.COLUMN_NAME_OBJECT_ID + " = ?";
        return db.rawQuery(SQL_RETURN_USER_DATA, new String[] {parseObjectId});
    }

    public Cursor returnLocationData(long location_ID) {
        final String SQL_RETURN_LOCATION_DATA = "SELECT * FROM " + LocationEntry.TABLE_NAME +
                " WHERE " + LocationEntry._ID + " = ?";
        return db.rawQuery(SQL_RETURN_LOCATION_DATA, new String[] {Long.toString(location_ID)});
    }

    public Cursor returnLocationData(String parseObjectId) {
        final String SQL_RETURN_LOCATION_DATA = "SELECT * FROM " + LocationEntry.TABLE_NAME +
                " WHERE " + LocationEntry.COLUMN_NAME_OBJECT_ID + " = ?";
        return db.rawQuery(SQL_RETURN_LOCATION_DATA, new String[] {parseObjectId});
    }

    public Cursor returnFeedData() {
        final String SQL_RETURN_FEED_DATA = "SELECT * FROM " + FeedEntry.TABLE_NAME;
        return db.rawQuery(SQL_RETURN_FEED_DATA, null);
    }

    public Cursor returnFeedData(String parseObjectId) {
        final String SQL_RETURN_FEED_DATA = "SELECT * FROM " + FeedEntry.TABLE_NAME +
                " WHERE " + FeedEntry._ID + " = ?";
        return db.rawQuery(SQL_RETURN_FEED_DATA, new String[] {parseObjectId});
    }

    /**
     * A SQLiteOpenHelper class to manage database creation and version management.
     */
    protected class DataBaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "famousDatabase";

        // If you change the database schema, you must increment the database version.
        private static final int DATABASE_VERSION = 3;

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Crates the tables
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {

            final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UserEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    UserEntry.COLUMN_NAME_USERNAME + " TEXT UNIQUE NOT NULL, " +
                    UserEntry.COLUMN_NAME_FULL_NAME + " TEXT NOT NULL, " +
                    UserEntry.COLUMN_NAME_PROFILE_PIC_URI + " TEXT UNIQUE " +
                    " );";

            final String SQL_CREATE_USER_DETAILS_TABLE = "CREATE TABLE " + UserDetailsEntry.TABLE_NAME + " (" +
                    UserDetailsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UserDetailsEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    UserDetailsEntry.COLUMN_NAME_WEBSITE + " TEXT, " +
                    UserDetailsEntry.COLUMN_NAME_BIO + " TEXT, " +
                    UserDetailsEntry.COLUMN_NAME_GENDER + " TEXT, " +
                    UserDetailsEntry.COLUMN_NAME_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    UserDetailsEntry.COLUMN_NAME_PHONE_NUMBER + " TEXT, " +
                    UserDetailsEntry.COLUMN_NAME_USER_KEY + " INTEGER UNIQUE NOT NULL, " +

                    // Setup the user_id as the foreign column to UserEntry table
                    " FOREIGN KEY (" + UserDetailsEntry.COLUMN_NAME_USER_KEY + ") REFERENCES " +
                    UserEntry.TABLE_NAME + " (" + UserEntry._ID + ") " +
                    " );";

            final String SQL_CREATE_FEED_TABLE = "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FeedEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    FeedEntry.COLUMN_NAME_CREATED_AT + " INTEGER, " +
                    FeedEntry.COLUMN_NAME_MEDIA_URI + " TEXT UNIQUE, " +
                    FeedEntry.COLUMN_NAME_TAGS + " TEXT, " +
                    FeedEntry.COLUMN_NAME_LOCATION_KEY + " INTEGER, " +
                    FeedEntry.COLUMN_NAME_USER_KEY + " INTEGER, " +

                    // Setup the location_id as the foreign column to LocationEntry table
                    " FOREIGN KEY (" + FeedEntry.COLUMN_NAME_LOCATION_KEY + ") REFERENCES " +
                    LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +

                    // Setup the user_id as the foreign column to UserEntry table
                    " FOREIGN KEY (" + FeedEntry.COLUMN_NAME_USER_KEY + ") REFERENCES " +
                    UserEntry.TABLE_NAME + " (" + UserEntry._ID + ") " +

                    " );";

            final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                    LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LocationEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    LocationEntry.COLUMN_NAME_LATITUDE + " DOUBLE NOT NULL, " +
                    LocationEntry.COLUMN_NAME_LONGITUDE + " DOUBLE NOT NULL, " +
                    LocationEntry.COLUMN_NAME_NAME + " STRING " +
                    " );";

            final String SQL_CREATE_COMMENT_TABLE = "CREATE TABLE " + CommentEntry.TABLE_NAME + " (" +
                    CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CommentEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    CommentEntry.COLUMN_NAME_CREATED_AT + " INTEGER NOT NULL, " +
                    CommentEntry.COLUMN_NAME_TEXT + " TEXT NOT NULL, " +
                    CommentEntry.COLUMN_NAME_FEED_KEY + " INTEGER, " +
                    CommentEntry.COLUMN_NAME_USER_KEY + " INTEGER, " +

                    // Setup the feed_id as the foreign column to FeedEntry table
                    " FOREIGN KEY (" + CommentEntry.COLUMN_NAME_FEED_KEY + ") REFERENCES " +
                    FeedEntry.TABLE_NAME + " (" + FeedEntry._ID + "), " +

                    // Setup the user_id as the foreign column to UserEntry table
                    " FOREIGN KEY (" + CommentEntry.COLUMN_NAME_USER_KEY + ") REFERENCES " +
                    UserEntry.TABLE_NAME + " (" + UserEntry._ID + ") " +
                    " );";

            final String SQL_CREATE_LIKES_TABLE = "CREATE TABLE " + LikesEntry.TABLE_NAME + " (" +
                    LikesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LikesEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    LikesEntry.COLUMN_NAME_USER_KEY + " INTEGER, " +
                    LikesEntry.COLUMN_NAME_FEED_KEY + " INTEGER, " +

                    // Setup the user_id as the foreign column to UserEntry table
                    " FOREIGN KEY (" + LikesEntry.COLUMN_NAME_USER_KEY + ") REFERENCES " +
                    UserEntry.TABLE_NAME + " (" + UserEntry._ID + "), " +

                    // Setup the user_id as the foreign column to UserEntry table
                    " FOREIGN KEY (" + LikesEntry.COLUMN_NAME_FEED_KEY + ") REFERENCES " +
                    FeedEntry.TABLE_NAME + " (" + FeedEntry._ID + ") " +

                    " );";

            try {
                db.execSQL(SQL_CREATE_USER_TABLE);
                db.execSQL(SQL_CREATE_USER_DETAILS_TABLE);
                db.execSQL(SQL_CREATE_FEED_TABLE);
                db.execSQL(SQL_CREATE_LOCATION_TABLE);
                db.execSQL(SQL_CREATE_COMMENT_TABLE);
                db.execSQL(SQL_CREATE_LIKES_TABLE);

            } catch(SQLiteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + UserDetailsEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CommentEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + LikesEntry.TABLE_NAME);

            onCreate(db);
        }
    }
}
