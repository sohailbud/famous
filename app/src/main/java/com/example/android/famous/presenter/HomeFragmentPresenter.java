package com.example.android.famous.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.famous.callback.FeedDataListener;
import com.example.android.famous.callback.UpdateUserRelationshipsResponse;
import com.example.android.famous.model.Feed;
import com.example.android.famous.model.User;
import com.example.android.famous.callback.SuggestedUserDataListener;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Sohail on 9/24/15.
 */
public class HomeFragmentPresenter {

    private static HomeFragmentPresenter homeFragmentPresenter = null;

    /**
     * Empty constructor
     */
    private HomeFragmentPresenter() {
        super();
    }

    /**
     * Create static instance of current class
     *
     * @return return static instance of current class
     */
    public static HomeFragmentPresenter getInstance() {
        if (homeFragmentPresenter == null) homeFragmentPresenter = new HomeFragmentPresenter();
        return homeFragmentPresenter;
    }

    /**
     * Executes the AsyncTask to fetch suggested user data from parse
     *
     * @param instance instance of interface that received the callback data
     */
    public void getSuggestedUserData(SuggestedUserDataListener instance) {
        GetSuggestedUserDataAsyncTask getSuggestedUserData = new GetSuggestedUserDataAsyncTask();
        getSuggestedUserData.delegate = instance;
        getSuggestedUserData.execute();
    }

    /**
     * Executes the AsyncTask to update user relationship arrays on parse
     *
     * @param instance instance of interface that received the callback data
     * @param objectId objectId of user current user clicked on
     * @param status   current tag associated with the button (FOLLOW / UNFOLLOW)
     */
    public void updateUserRelationships(
            UpdateUserRelationshipsResponse instance, String objectId, String status) {
        UpdateUserRelationshipsAsyncTask updateUserRelationships = new UpdateUserRelationshipsAsyncTask();
        updateUserRelationships.delegate = instance;
        updateUserRelationships.execute(objectId, status);
    }

    public void getFeedData(FeedDataListener instance) {
        GetFeedDataAsyncTask getFeedData = new GetFeedDataAsyncTask();
        getFeedData.delegate = instance;
        getFeedData.execute();
    }

    /**
     * 1. AsyncTask class to fetch the suggestion data from parse in background
     */
    private class GetSuggestedUserDataAsyncTask extends AsyncTask<Void, Void, List<User>> {

        // Callback variable
        private SuggestedUserDataListener delegate = null;

        /**
         * Query all users data from parse
         *
         * @param params null
         * @return list of all ParseUsers and current parse user's Details object
         */
        @Override
        protected List<User> doInBackground(Void... params) {

            // list of all parse users
            ParseQuery<ParseUser> parseUserQuery = ParseUser.getQuery();
            List<ParseUser> parseUserList = null;
            try {
                parseUserList = parseUserQuery.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Build query to get current user details
            ParseQuery<ParseObject> parseCurrentUserDetailsQuery = ParseQuery.getQuery("UserDetails");
            parseCurrentUserDetailsQuery.whereEqualTo("User", ParseUser.getCurrentUser());

            List<ParseUser> currentUserFollowsList = new ArrayList<>();
            try {
                // get current user details object
                ParseObject parseCurrentUserDetails = parseCurrentUserDetailsQuery.getFirst();

                // build relation query and get current user's follows list
                ParseRelation<ParseUser> currentUserRelationFollows = parseCurrentUserDetails.getRelation("follows");
                ParseQuery query = currentUserRelationFollows.getQuery();
                currentUserFollowsList.addAll(query.find());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            // add current user to the follows list to make checking easy
            currentUserFollowsList.add(ParseUser.getCurrentUser());

            // empty list to pass User objects
            List<User> userList = new ArrayList<>();

            // filter out parse users that current user is not following from complete users list
            if (parseUserList != null) {
                for (ParseUser parseUser : parseUserList) {
                    if (!currentUserFollowsList.contains(parseUser)) {
                        userList.add(new User(parseUser.getObjectId(),
                                parseUser.getUsername(), parseUser.get("fullName").toString()));
                    }
                }
            }

            return userList;
        }

        /**
         * Convert parse users to regular users and insert them in a list while ignoring current user
         * Call the callback method to pass the data
         *
         * @param userList consists list of all ParseUsers
         */
        @Override
        protected void onPostExecute(List<User> userList) {
            super.onPostExecute(userList);

            // add data to callback variable
            delegate.suggestedUserDataOnProcess(userList);
        }
    }

    /**
     * 2. AsyncTask class to update user relationships on parse in background
     */
    private class UpdateUserRelationshipsAsyncTask extends AsyncTask<String, Void, UpdateUserRelationshipsAsyncTask.Wrapper> {

        // Callback variable
        private UpdateUserRelationshipsResponse delegate = null;

        /**
         * Updates relationship arrays in UserDetails object of users involved
         *
         * @param params contains object id of user current user clicked on, status to FOLLOW or UNFOLLOW
         * @return true if arrays were updated successfully on parse, true/false based on to follow or to unfollow
         */
        @Override
        protected Wrapper doInBackground(String... params) {

            String status = params[1]; // intended change on relationship status
            final String FOLLOW = "FOLLOW";
            final String UN_FOLLOW = "UN_FOLLOW";

            // store results in wrapper class
            Wrapper results = new Wrapper();

            // Get other user object
            ParseQuery<ParseUser> parseUserQuery = ParseUser.getQuery();
            ParseUser parseOtherUser = null;
            try {
                parseOtherUser = parseUserQuery.get(params[0]);
            } catch (ParseException e) {
                e.printStackTrace();
                results.successfullyUpdated = false;

            }

            // Get current user and other user details object
            // Create query to get user details
            ParseQuery<ParseObject> parseCurrentUserDetailsQuery = ParseQuery.getQuery("UserDetails");
            ParseQuery<ParseObject> parseOtherUserDetailsQuery = ParseQuery.getQuery("UserDetails");

            // Condition the user details query
            parseCurrentUserDetailsQuery.whereEqualTo("User", ParseUser.getCurrentUser());
            parseOtherUserDetailsQuery.whereEqualTo("User", parseOtherUser);

            try {
                // get the user details object
                ParseObject parseCurrentUserDetails = parseCurrentUserDetailsQuery.getFirst();
                ParseObject parseOtherUserDetails = parseOtherUserDetailsQuery.getFirst();

                // Create current and other user relation object
                ParseRelation<ParseUser> currentUserRelationFollows = parseCurrentUserDetails.getRelation("follows");
                ParseRelation<ParseUser> otherUserRelationFollowedBy = parseOtherUserDetails.getRelation("followedBy");

                // update relationship based on current user's requested change
                if (status.equals(FOLLOW)) {
                    currentUserRelationFollows.add(parseOtherUser);
                    otherUserRelationFollowedBy.add(ParseUser.getCurrentUser());
                    results.status = UN_FOLLOW;

                } else if (status.equals(UN_FOLLOW)) {
                    currentUserRelationFollows.remove(parseOtherUser);
                    otherUserRelationFollowedBy.remove(ParseUser.getCurrentUser());
                    results.status = FOLLOW;
                }

                // Save both user details object
                parseCurrentUserDetails.save();
                parseOtherUserDetails.save();

                results.successfullyUpdated = true;

            } catch (ParseException e) {
                e.printStackTrace();
                results.successfullyUpdated = false;
            }

            return results;

        }

        /**
         * Gets data from doInBackground and sets it to callback method
         *
         * @param wrapper boolean for parse update, updated status for future requests
         */
        @Override
        protected void onPostExecute(Wrapper wrapper) {
            super.onPostExecute(wrapper);
            delegate.userRelationshipsUpdated(wrapper.successfullyUpdated, wrapper.status);
        }


        /**
         * Wrapper class to pass two values from doInBackground to onPostExecute
         */
        protected class Wrapper {
            private boolean successfullyUpdated;
            private String status;
        }
    }

    /**
     * 3. AsyncTask class to fetch feed dara from parse in background
     */
    private class GetFeedDataAsyncTask extends AsyncTask<Void, Void, List<ParseObject>> {

        private FeedDataListener delegate = null;

        @Override
        protected List<ParseObject> doInBackground(Void... params) {

//            // UserDetails of current user
//            List<ParseObject> parseUserDetailsList = getParseUserDetails(
//                    new String[] {ParseUser.getCurrentUser().getObjectId()}, 1);
//
//            List<String> followsList = null;
//            if (parseUserDetailsList != null) {
//                followsList = (List<String>) parseUserDetailsList.get(0).get("follows");
//            }
//
//            List<ParseObject> parseFeedDataList = getParseFeedData(followsList);
//
//            // Convert parse Feed object to java Feed objects
//            if (parseFeedDataList != null) {
//                List<Feed> feedDataList = new ArrayList<>();
//
//                for (ParseObject parseFeedData : parseFeedDataList) {
////                    User user = new User();
////                    Feed feed = new Feed(null, );
//                }
//            }

            return null;
        }

        @Override
        protected void onPostExecute(List<ParseObject> parseFeedDataList) {
            super.onPostExecute(parseFeedDataList);


        }
    }

}