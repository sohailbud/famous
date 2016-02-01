package com.example.android.famous.interactor;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.famous.R;
import com.example.android.famous.adapter.UserListRecyclerViewAdapter;
import com.example.android.famous.model.User;
import com.example.android.famous.util.parse.ParseHelper;
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

    private static UserRelationshipInteractor userRelationshipInteractor = null;

    public static UserRelationshipInteractor getInstance() {
        if (userRelationshipInteractor == null)
            userRelationshipInteractor = new UserRelationshipInteractor();

        return userRelationshipInteractor;
    }

    private UserRelationshipInteractor() {
    }

    /**
     * 1. AsyncTask class to fetch the suggestion data from parse in background
     */
    public class GetSuggestedUserDataTask extends AsyncTask<Void, Void, List<User>> {

        // Callback variable
//        private SuggestedUserDataListener delegate = null;

        private UserListRecyclerViewAdapter userListRecyclerViewAdapter;

        public GetSuggestedUserDataTask(UserListRecyclerViewAdapter userListRecyclerViewAdapter) {
            this.userListRecyclerViewAdapter = userListRecyclerViewAdapter;
        }

        /**
         * Query all users data from parse
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
                        try {
                            userList.add(new User(parseUser.getObjectId(),
                                    parseUser.getUsername(), parseUser.get("fullName").toString()));
                        } catch (NullPointerException e) {

                        }

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
//            delegate.suggestedUserDataOnProcess(userList);
            userListRecyclerViewAdapter.swapData(userList);
        }
    }

    /**
     * 2. AsyncTask class to update user relationships on parse in background
     */
    public class UpdateUserRelationshipTask extends AsyncTask<Object, Void, UpdateUserRelationshipTask.Wrapper> {

        /**
         * Updates relationship arrays in UserDetails object of users involved
         * @param params contains object id of user current user clicked on, status to FOLLOW or UNFOLLOW
         * @return true if arrays were updated successfully on parse and updated status
         */
        @Override
        protected Wrapper doInBackground(Object... params) {

            String objectId = (String) params[0];
            View view = (View) params[1];
            String status = (String) params[2]; // intended change on relationship status

            final String FOLLOW = "FOLLOW";
            final String UN_FOLLOW = "UN_FOLLOW";

            // store results in wrapper class
            Wrapper results = new Wrapper();
            results.view = view;

            try {
                // Get other user object
                ParseQuery<ParseUser> parseUserQuery = ParseUser.getQuery();
                ParseUser parseOtherUser = parseUserQuery.get(objectId);

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

            userRelationshipsUpdated(wrapper);
        }

        public void userRelationshipsUpdated(Wrapper wrapper) {

            final String FOLLOW = "FOLLOW";
            final String UN_FOLLOW = "UN_FOLLOW";

            String follow = wrapper.view.getContext().getResources().getString(R.string.follow);
            String following = wrapper.view.getContext().getResources().getString(R.string.following);

            if (wrapper.successfullyUpdated && wrapper.status.equals(UN_FOLLOW)) {
                ((Button) wrapper.view).setText(following);
                wrapper.view.setTag(UN_FOLLOW);
            }
            else if (wrapper.successfullyUpdated && wrapper.status.equals(FOLLOW)) {
                ((Button) wrapper.view).setText(follow);
                wrapper.view.setTag(FOLLOW);
            }
            wrapper.view.setClickable(true);
        }

        /**
         * Wrapper class to pass two values from doInBackground to onPostExecute
         */
        protected class Wrapper {
            private boolean successfullyUpdated;
            private String status;
            private View view;
        }
    }
}
