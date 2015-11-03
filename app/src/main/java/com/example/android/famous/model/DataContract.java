package com.example.android.famous.model;

import android.provider.BaseColumns;

/**
 * Created by Sohail on 10/29/15.
 */
public class DataContract {

    public static final class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user_entry";
        public static final String COLUMN_NAME_OBJECT_ID = "object_id";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_EMAIL = "fullname";
        public static final String COLUMN_NAME_PROFILE_PIC_URI = "profile_pic_URI";

    }

    public static final class UserDetailsEntry implements BaseColumns {
        public static final String TABLE_NAME = "user_details_entry";
        public static final String COLUMN_NAME_OBJECT_ID = "object_id";
        public static final String COLUMN_NAME_USER_KEY = "user_id";
        public static final String COLUMN_NAME_WEBSITE = "website";
        public static final String COLUMN_NAME_BIO = "bio";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";

    }

    public static final class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "feed_entry";
        public static final String COLUMN_NAME_OBJECT_ID = "object_id";
        public static final String COLUMN_NAME_CREATED_AT = "created_at";
        public static final String COLUMN_NAME_LOCATION_KEY = "location_id";
        public static final String COLUMN_NAME_USER_KEY = "user_id";
        public static final String COLUMN_NAME_MEDIA_URI = "media_URI";
        public static final String COLUMN_NAME_COMMENT_KEY = "comment_id";
        public static final String COLUMN_NAME_LIKE_USER_KEY = "like_user_id";
        public static final String COLUMN_NAME_TAGS = "tags";
//        public static final String COLUMN_NAME_USERS_IN_PHOTO_LIST = "usersInPhotoList";

    }

    public static final class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "locationEntry";
        public static final String COLUMN_NAME_OBJECT_ID = "objectId";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_NAME = "name";

    }

    public static final class CommentEntry implements BaseColumns {
        public static final String TABLE_NAME = "commentEntry";
        public static final String COLUMN_NAME_OBJECT_ID = "objectId";
        public static final String COLUMN_NAME_CREATED_AT = "createdAt";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_USER_KEY = "user_id";

    }
}
