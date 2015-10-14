package com.example.android.famous.presenter;

import android.os.AsyncTask;

import com.example.android.famous.callback.UpdateUserRelationshipsResponse;
import com.example.android.famous.model.User;
import com.example.android.famous.callback.SuggestedUserDataListener;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
     * @return return static instance of current class
     */
    public static HomeFragmentPresenter getInstance() {
        if (homeFragmentPresenter == null) homeFragmentPresenter = new HomeFragmentPresenter();
        return homeFragmentPresenter;
    }

    /**
     * Executes the AsyncTask to fetch suggested user data from parse
     * @param instance instance of interface that received the callback data
     */
    public void getSuggestedUserData(SuggestedUserDataListener instance) {
        SuggestedUserDataAsyncTask suggestedUserData = new SuggestedUserDataAsyncTask();
        suggestedUserData.delegate = instance;
        suggestedUserData.execute();
    }

    /**
     * Executes the AsyncTask to update user relationship arrays on parse
     * @param instance instance of interface that received the callback data
     * @param objectId objectId of user current user clicked on
     * @param status current tag associated with the button (FOLLOW / UNFOLLOW)
     */
    public void updateUserRelationships(
            UpdateUserRelationshipsResponse instance, String objectId, String status) {

        UpdateUserRelationships updateUserRelationships = new UpdateUserRelationships();
        updateUserRelationships.delegate = instance;
        updateUserRelationships.execute(objectId, status);
    }

    /**
     * AsyncTask class to get fetch the suggestion data from parse in background
     */
    private class SuggestedUserDataAsyncTask extends AsyncTask<Void, Void, SuggestedUserDataAsyncTask.Wrapper> {

        // Callback variable
        private SuggestedUserDataListener delegate = null;

        /**
         * Query all users data from parse
         * @param params null
         * @return list of all ParseUsers and current parse user's Details object
         */
        @Override
        protected Wrapper doInBackground(Void... params) {

            // list of all parse users
            ParseQuery<ParseUser> parseUserQuery = ParseUser.getQuery();
            List<ParseUser> parseUserList = null;

            // UserDetails of current user
            ParseQuery<ParseObject> parseUserDetailsQuery = ParseQuery.getQuery("UserDetails");
            parseUserDetailsQuery.whereEqualTo("user", ParseUser.getCurrentUser().getObjectId());
            ParseObject parseUserDetails = null;

            // fetch the data from parse
            try {
                parseUserList = parseUserQuery.find();
                parseUserDetails = parseUserDetailsQuery.getFirst();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // store data in wrapper class
            Wrapper wrapper = new Wrapper();
            wrapper.parseUserList = parseUserList;
            wrapper.parseUserDetails = parseUserDetails;

            return wrapper;
        }

        /**
         * Convert parse users to regular users and insert them in a list while ignoring current user
         * Call the callback method to pass the data
         * @param wrapper consists list of all ParseUsers and current parse user's Details object
         */
        @Override
        protected void onPostExecute(Wrapper wrapper) {
            super.onPostExecute(wrapper);

            // extract data form wrapper class
            List<ParseUser> parseUserList = wrapper.parseUserList;
            ParseObject parseUserDetails = wrapper.parseUserDetails;

            // create a list containing ObjectId of current user and every user current user follows
            List<String> userFollowsList = new ArrayList<>();
            userFollowsList.add(ParseUser.getCurrentUser().getObjectId());
            try {
                userFollowsList.addAll((List<String>) parseUserDetails.get("follows"));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            // using above list, create a new list of Users which current user is not following
            List<User> users = new ArrayList<>();
            for (ParseUser parseUser : parseUserList) {
                if (!userFollowsList.contains(parseUser.getObjectId())) {
                    User user = new User(parseUser.getObjectId(),
                            parseUser.getUsername(),
                            parseUser.get("fullName").toString());

                    users.add(user);
                }
            }

            // add data to callback variable
            delegate.suggestedUserDataOnProcess(users);
        }

        /**
         * Wrapper class to pass two values from doInBackground to onPostExecute
         */
        protected class Wrapper {
            private List<ParseUser> parseUserList;
            private ParseObject parseUserDetails;
        }
    }

    /**
     * AsyncTask class to update user relationships on parse in background
     */
    private class UpdateUserRelationships extends AsyncTask<String, Void, UpdateUserRelationships.Wrapper> {

        // Callback variable
        private UpdateUserRelationshipsResponse delegate = null;

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
         * Updates relationship arrays in UserDetails object of users involved
         * @param params contains object id of user current user clicked on, status to FOLLOW or UNFOLLOW
         * @return true if arrays were updated successfully on parse, true/false based on to follow or to unfollow
         */
        @Override
        protected Wrapper doInBackground(String... params) {

            String status = params[1]; // intended change on relationship status
            final String FOLLOW = "FOLLOW";
            final String UNFOLLOW = "UNFOLLOW";

            // store results in wrapper class
            Wrapper results = new Wrapper();

            // array containing objectId of current user and other user
            String[] filter = {ParseUser.getCurrentUser().getObjectId(), params[0]};
            // List to hold UserDetails object of these two users
            List<ParseObject> parseUserDetailsObjectList;

            // Query UserDetails object of these two users
            ParseQuery<ParseObject> userDetailsQuery = ParseQuery.getQuery("UserDetails");
            userDetailsQuery.whereContainedIn("user", Arrays.asList(filter));

            try {
                parseUserDetailsObjectList = userDetailsQuery.find();

                // checks to see if UserDetails object for both users were fetched
                if (parseUserDetailsObjectList.size() == 2) {

                    // loop through current user and other user's details object
                    for (int i = 0; i < 2; i++) {
                        ParseObject parseUserDetailsObject = parseUserDetailsObjectList.get(i);

                        // based on current user's intended change to Follow or Un Follow other user
                        // "follows" array current user and "followedBy" of other user gets updated
                        if (status.equals(FOLLOW)) {
                            if (parseUserDetailsObject.get("user").equals(filter[1]))
                                parseUserDetailsObject.add("followedBy", filter[0]);
                            else if (parseUserDetailsObject.get("user").equals(filter[0]))
                                parseUserDetailsObject.add("follows", filter[1]);

                            // update the status for future requests
                            results.status = UNFOLLOW;

                        } else if (status.equals(UNFOLLOW)) {
                            if (parseUserDetailsObject.get("user").equals(filter[1]))
                                parseUserDetailsObject.removeAll("followedBy", Collections.singletonList(filter[0]));
                            else if (parseUserDetailsObject.get("user").equals(filter[0]))
                                parseUserDetailsObject.removeAll("follows", Collections.singletonList(filter[1]));

                            results.status = FOLLOW;
                        }
                        // Update the list with updated data in UserDetails object
                        parseUserDetailsObjectList.set(i, parseUserDetailsObject);
                    }
                    // Save the data to parse
                    ParseObject.saveAll(parseUserDetailsObjectList);
                    results.successfullyUpdated = true;
                    return results;

                } else {
                    // if user details object list doesnt contain object for both users
                    results.successfullyUpdated = false;
                    return results;
                }

            } catch (ParseException e) {
                e.printStackTrace();
                results.successfullyUpdated = false;
                return results;
            }
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
