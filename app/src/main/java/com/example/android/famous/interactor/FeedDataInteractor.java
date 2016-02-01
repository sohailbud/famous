package com.example.android.famous.interactor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.famous.adapter.FeedRecyclerViewAdapter;
import com.example.android.famous.callback.FeedFragmentInterface;
import com.example.android.famous.model.Feed;
import com.example.android.famous.model.Location;
import com.example.android.famous.model.User;
import com.example.android.famous.presenter.AccessPresenter;
import com.example.android.famous.util.parse.ParseHelper;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 10/29/15.
 */
public class FeedDataInteractor extends ParseHelper {

    private static FeedDataInteractor feedDataInteractor = null;

    private FeedDataInteractor() {
    }

    public static FeedDataInteractor getInstance() {
        if (feedDataInteractor == null) feedDataInteractor = new FeedDataInteractor();

        return feedDataInteractor;
    }

    /**
     * AsyncTask class to fetch feed dara from parse in background
     */
    public class GetFeedDataTask extends AsyncTask<Void, Void, List<Feed>> {

        private FeedRecyclerViewAdapter feedRecyclerViewAdapter;

        public GetFeedDataTask(FeedRecyclerViewAdapter feedRecyclerViewAdapter) {
            this.feedRecyclerViewAdapter = feedRecyclerViewAdapter;
        }

        /**
         * gets current users follows list and cross-references it with feed table to get feed data
         * @param params null
         * @return a list of Feed objects
         */
        @Override
        protected List<Feed> doInBackground(Void... params) {
            List<Feed> feedDataList = new ArrayList<>();

            try {
                // get current user details object
                ParseObject parseCurrentUserDetails = getParseUserDetails(ParseUser.getCurrentUser());
                // get user's follows list
                List<ParseUser> currentUserFollowsList = getParseUserFollowsList(parseCurrentUserDetails);

                // get feed data for the follows list
                ParseQuery<ParseObject> parseFeedDataQuery = ParseQuery.getQuery("Feed");
                parseFeedDataQuery.whereContainedIn("createdBy", currentUserFollowsList);

                // get the feed data from parse
                List<ParseObject> parseFeedDataList = parseFeedDataQuery.find();

                // convert parse feed data to java feed objects
                if (parseFeedDataList != null) {
                    for (ParseObject parseFeedData : parseFeedDataList) {

                        // get location data
                        ParseObject parseLocation = (ParseObject) parseFeedData.get("Location");

                        ParseGeoPoint parseGeoPoint = parseLocation.fetchIfNeeded().getParseGeoPoint("geoPoint");
                        Location location = new Location(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude());
                        location.setObjectId(parseLocation.getObjectId());
                        location.setName((String) parseLocation.get("name"));

                        // get user object
                        ParseUser createdBy = (ParseUser) parseFeedData.get("createdBy");
                        User user = AccessPresenter.convertParseUserToUser(createdBy);

                        // create new feed object and get supporting data
                        Feed feed = new Feed(location, user, null);
                        feed.setCreatedAt((String) parseFeedData.get("createdAt"));
                        feed.setTags((String) parseFeedData.get("tags"));
                        feed.setObjectId(parseFeedData.getObjectId());

                        feedDataList.add(feed);

//                        // get image
//                        ParseFile media = (ParseFile) parseFeedData.get("media");
//                        byte[] data = media.getData();
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
                Log.i("EXCEPTION", "GetFeedDataAsyncTask > doInBackground");
            }

            return feedDataList;
        }

        @Override
        protected void onPostExecute(List<Feed> feedDataList) {
            super.onPostExecute(feedDataList);

            feedRecyclerViewAdapter.insertData(feedDataList);
        }
    }
}
