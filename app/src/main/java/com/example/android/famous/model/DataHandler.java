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

    public DataHandler(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
    }

    public DataHandler open() {
        db = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBaseHelper.close();
    }

    public long insertUserData(User user) {

        Cursor userCursor = returnUserData(user.getObjectId());

        if (userCursor.getCount() == -1) {
            long row_ID = userCursor.getLong(userCursor.getColumnIndex("_ID"));
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

    public long insertLocationData(Location location) {
        ContentValues locationValues = new ContentValues();
        locationValues.put(LocationEntry.COLUMN_NAME_OBJECT_ID, location.getObjectId());
        locationValues.put(LocationEntry.COLUMN_NAME_LATITUDE, location.getLatitude());
        locationValues.put(LocationEntry.COLUMN_NAME_LONGITUDE, location.getLongitude());
        locationValues.put(LocationEntry.COLUMN_NAME_NAME, location.getName());

        return db.insert(LocationEntry.TABLE_NAME, null, locationValues);
    }

    public long insertFeedData(Feed feed, long user_ID, long location_ID) {
        ContentValues feedValues = new ContentValues();
        feedValues.put(FeedEntry.COLUMN_NAME_OBJECT_ID, feed.getObjectId());
        feedValues.put(FeedEntry.COLUMN_NAME_CREATED_AT, feed.getCreatedAt());
        feedValues.put(FeedEntry.COLUMN_NAME_LOCATION_KEY, location_ID);
        feedValues.put(FeedEntry.COLUMN_NAME_USER_KEY, user_ID);
        feedValues.put(FeedEntry.COLUMN_NAME_MEDIA_URI, feed.getMediaURI());
        feedValues.put(FeedEntry.COLUMN_NAME_TAGS, feed.getTags());

        return db.insert(FeedEntry.TABLE_NAME, null, feedValues);
    }

    public Cursor returnUserData(long user_ID) {
        final String SQL_RETURN_USER_DATA = "SELECT * FROM " + UserEntry.TABLE_NAME +
                " WHERE " + UserEntry._ID + " = " + user_ID;
        return db.rawQuery(SQL_RETURN_USER_DATA, null);
    }

    public Cursor returnUserData(String parseObjectId) {
        final String SQL_RETURN_USER_DATA = "SELECT * FROM " + UserEntry.TABLE_NAME +
                " WHERE " + UserEntry.COLUMN_NAME_OBJECT_ID + " = " + '"' + parseObjectId + '"';
        return db.rawQuery(SQL_RETURN_USER_DATA, null);
    }

    public Cursor returnLocationData(long location_ID) {
        final String SQL_RETURN_LOCATION_DATA = "SELECT * FROM " + LocationEntry.TABLE_NAME +
                " WHERE " + LocationEntry._ID + " = " + location_ID;
        return db.rawQuery(SQL_RETURN_LOCATION_DATA, null);
    }

    public Cursor returnFeedData() {
        final String SQL_RETURN_FEED_DATA = "SELECT * FROM " + FeedEntry.TABLE_NAME;
        return db.rawQuery(SQL_RETURN_FEED_DATA, null);

    }

    protected class DataBaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "famousDatabase";

        // If you change the database schema, you must increment the database version.
        private static final int DATABASE_VERSION = 17;

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

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
                    FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    FeedEntry.COLUMN_NAME_CREATED_AT + " INTEGER NOT NULL, " +
                    FeedEntry.COLUMN_NAME_MEDIA_URI + " TEXT UNIQUE NOT NULL, " +
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
