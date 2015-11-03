package com.example.android.famous.interactor;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.famous.callback.SuggestedUserDataListener;
import com.example.android.famous.callback.UpdateUserRelationshipsResponse;
import com.example.android.famous.model.User;
import com.example.android.famous.util.ParseHelper;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 10/30/15.
 */
public class UserRelationshipInteractor extends ParseHelper {

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

            List<ParseUser> currentUserFollowsList = new ArrayList<>();
            try {
                parseUserList = parseUserQuery.find();

                // get current user details object from parse
                ParseObject parseCurrentUserDetails = getParseUserDetails(ParseUser.getCurrentUser());
                // get current user's follows list from parse
                currentUserFollowsList.addAll(getParseUserFollowsList(parseCurrentUserDetails));

            } catch (ParseException e) {
                e.printStackTrace();
                Log.i("EXCEPTION", "GetSuggestedUserDataAsyncTask > doInBackground");
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
         * @param params contains object id of user current user clicked on, status to FOLLOW or UNFOLLOW
         * @return true if arrays were updated successfully on parse and updated status
         */
        @Override
        protected Wrapper doInBackground(String... params) {

            String status = params[1]; // intended change on relationship status
            final String FOLLOW = "FOLLOW";
            final String UN_FOLLOW = "UN_FOLLOW";

            // store results in wrapper class
            Wrapper results = new Wrapper();

            try {
                // Get other user object
                ParseQuery<ParseUser> parseUserQuery = ParseUser.getQuery();
                ParseUser parseOtherUser = parseUserQuery.get(params[0]);

                // get the user details object
                ParseObject parseCurrentUserDetails = getParseUserDetails(ParseUser.getCurrentUser());
                ParseObject parseOtherUserDetails = getParseUserDetails(parseOtherUser);

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
                Log.i("Exception", "UpdateUserRelationshipsAsyncTask > doInBackground");
            }

            return results;
        }

        /**
         * Gets data from doInBackground and sets it to callback method
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
}
