package com.example.android.famous.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.famous.model.DataContract.UserEntry;
import com.example.android.famous.model.DataContract.UserDetailsEntry;
import com.example.android.famous.model.DataContract.FeedEntry;
import com.example.android.famous.model.DataContract.LocationEntry;
import com.example.android.famous.model.DataContract.CommentEntry;



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

    public long insertData(String tableName, ContentValues contentValues) {

        switch (tableName) {
            case UserEntry.TABLE_NAME:
                return db.insertOrThrow(UserEntry.TABLE_NAME, null, contentValues);
            case UserDetailsEntry.TABLE_NAME:
                return db.insertOrThrow(UserDetailsEntry.TABLE_NAME, null, contentValues);
            case FeedEntry.TABLE_NAME:
                return db.insertOrThrow(FeedEntry.TABLE_NAME, null, contentValues);
            case LocationEntry.TABLE_NAME:
                return db.insertOrThrow(LocationEntry.TABLE_NAME, null, contentValues);
            case CommentEntry.TABLE_NAME:
                return db.insertOrThrow(CommentEntry.TABLE_NAME, null, contentValues);
            default:
                return -1;
        }
    }

    protected class DataBaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "famousDatabase";

        // If you change the database schema, you must increment the database version.
        private static final int DATABASE_VERSION = 1;

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    UserEntry.COLUMN_NAME_USERNAME + " TEXT UNIQUE NOT NULL, " +
                    UserEntry.COLUMN_NAME_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    UserEntry.COLUMN_NAME_PROFILE_PIC_URI + " TEXT UNIQUE " +
                    " );";

            final String SQL_CREATE_USER_DETALS_TABLE = "CREATE_TABLE " + UserDetailsEntry.TABLE_NAME + " (" +
                    UserDetailsEntry._ID + " INTEGER PRIMARY KEY," +
                    UserDetailsEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    UserDetailsEntry.COLUMN_NAME_WEBSITE + " TEXT, " +
                    UserDetailsEntry.COLUMN_NAME_BIO + " TEXT, " +
                    UserDetailsEntry.COLUMN_NAME_GENDER + " TEXT, " +
                    UserDetailsEntry.COLUMN_NAME_EMAIL + " TEXT, " +
                    UserDetailsEntry.COLUMN_NAME_PHONE_NUMBER + " TEXT " +

                    // Setup the user_id as the foreign column to UserEntry table
                    " FOREIGN KEY (" + UserDetailsEntry.COLUMN_NAME_USER_KEY + ") REFERENCES " +
                    UserEntry.TABLE_NAME + " (" + UserEntry._ID + "), " +
                    " );";

            final String SQL_CREATE_FEED_TABLE = "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    FeedEntry.COLUMN_NAME_CREATED_AT + " INTEGER NOT NULL, " +
                    FeedEntry.COLUMN_NAME_MEDIA_URI + " TEXT UNIQUE NOT NULL, " +
                    FeedEntry.COLUMN_NAME_TAGS + " TEXT, " +

                    // Setup the location_id as the foreign column to LocationEntry table
                    " FOREIGN KEY (" + FeedEntry.COLUMN_NAME_LOCATION_KEY + ") REFERENCES " +
                    LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +

                    // Setup the user_id as the foreign column to UserEntry table
                    " FOREIGN KEY (" + FeedEntry.COLUMN_NAME_USER_KEY + ") REFERENCES " +
                    UserEntry.TABLE_NAME + " (" + UserEntry._ID + "), " +

                    // Setup the comment_id as the foreign column to CommentEntry table
                    " FOREIGN KEY (" + FeedEntry.COLUMN_NAME_COMMENT_KEY + ") REFERENCES " +
                    CommentEntry.TABLE_NAME + " (" + CommentEntry._ID + "), " +

                    // Setup the user_id as the foreign column to UserEntry table
                    " FOREIGN KEY (" + FeedEntry.COLUMN_NAME_LIKE_USER_KEY + ") REFERENCES " +
                    UserEntry.TABLE_NAME + " (" + UserEntry._ID + "), " +

                    " );";

            final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                    LocationEntry._ID + " INTEGER PRIMARY KEY," +
                    LocationEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    LocationEntry.COLUMN_NAME_LATITUDE + " REAL NOT NULL, " +
                    LocationEntry.COLUMN_NAME_LONGITUDE + " REAL NOT NULL, " +
                    LocationEntry.COLUMN_NAME_NAME + " STRING " +
                    " );";

            final String SQL_CREATE_COMMENT_TABLE = "CREATE TABLE " + CommentEntry.TABLE_NAME + " (" +
                    CommentEntry._ID + " INTEGER PRIMARY KEY," +
                    CommentEntry.COLUMN_NAME_OBJECT_ID + " TEXT UNIQUE NOT NULL, " +
                    CommentEntry.COLUMN_NAME_CREATED_AT + " INTEGER NOT NULL, " +
                    CommentEntry.COLUMN_NAME_TEXT + " TEXT NOT NULL, " +

                    // Setup the user_id as the foreign column to UserEntry table
                    " FOREIGN KEY (" + CommentEntry.COLUMN_NAME_USER_KEY + ") REFERENCES " +
                    UserEntry.TABLE_NAME + " (" + UserEntry._ID + "), " +
                    " );";

            try {
                db.execSQL(SQL_CREATE_USER_TABLE);
                db.execSQL(SQL_CREATE_USER_DETALS_TABLE);
                db.execSQL(SQL_CREATE_FEED_TABLE);
                db.execSQL(SQL_CREATE_LOCATION_TABLE);
                db.execSQL(SQL_CREATE_COMMENT_TABLE);

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

            onCreate(db);
        }
    }
}
