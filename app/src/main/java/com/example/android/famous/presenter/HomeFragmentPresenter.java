package com.example.android.famous.presenter;

import android.database.Cursor;

import com.example.android.famous.adapter.FeedRecyclerViewAdapter;
import com.example.android.famous.callback.FeedFragmentInterface;
import com.example.android.famous.fragment.common.FeedFragment;
import com.example.android.famous.interactor.FeedDataInteractor;
import com.example.android.famous.model.DataHandler;
import com.example.android.famous.model.Feed;
import com.example.android.famous.model.Location;
import com.example.android.famous.model.User;

import java.util.ArrayList;
import java.util.List;

import com.example.android.famous.model.DataContract.FeedEntry;
import com.example.android.famous.model.DataContract.LocationEntry;
import com.example.android.famous.model.DataContract.UserEntry;

/**
 * Created by Sohail on 9/24/15.
 */
public class HomeFragmentPresenter implements FeedFragmentInterface {

    private static HomeFragmentPresenter homeFragmentPresenter = null;
    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;
    private FeedFragment feedFragment;

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
     * Gets called from onPostExecute after asyncTask successfully grabs the data from parse
     * @param feedList
     */
    @Override
    public void feedDataOnProcess(List<Feed> feedList) {
        if (feedRecyclerViewAdapter != null && !feedList.isEmpty())
            feedRecyclerViewAdapter.swapData(feedList);
        insertInDatabase(feedList);

    }

    /**
     * This method gets called from the android UI and performs following tasks
     * 1. start asyncTask to get remote data
     * 2. get current data from sql and load it to the adapter
     * @param feedRecyclerViewAdapter
     * @param feedFragment
     */
    public void fetchDataForAdapter(FeedRecyclerViewAdapter feedRecyclerViewAdapter, FeedFragment feedFragment) {
        // 1. get data from server
        FeedDataInteractor.GetFeedDataTask feedDataTask = new FeedDataInteractor(). new GetFeedDataTask();
        feedDataTask.delegate = this;
        feedDataTask.execute();

        this.feedRecyclerViewAdapter = feedRecyclerViewAdapter;
        this.feedFragment = feedFragment;

        // get current sql data
        List<Feed> feedList = getSqlFeedData();
        if (!feedList.isEmpty()) feedRecyclerViewAdapter.swapData(feedList);

    }

    /**
     * Loads the provided feed data into sql database
     * @param feedList
     */
    private void insertInDatabase(List<Feed> feedList) {

        // insert into sql database
        DataHandler dataHandler = new DataHandler(feedFragment.getActivity());
        dataHandler.open();
        for (Feed feed : feedList) {
            long user_ID = dataHandler.insertUserData(feed.getUser());
            long location_ID = dataHandler.insertLocationData(feed.getLocation());
            if (user_ID != -1 && location_ID != -1) {
                dataHandler.insertFeedData(feed, user_ID, location_ID);
            }
        }
        dataHandler.close();
    }

    /**
     * Get the data from sql database
     * @return
     */
    private List<Feed> getSqlFeedData() {
        DataHandler dataHandler = new DataHandler(feedFragment.getActivity());
        dataHandler.open();

        // get all feed data from the database
        Cursor cursorFeed = dataHandler.returnFeedData();

        List<Feed> feedList = new ArrayList<>();

        // convert feed data from cursors to java objects
        while (cursorFeed.moveToNext()) {

            // get reference to location and user from the feed cursor
            long location_ID = cursorFeed.getInt(cursorFeed.getColumnIndex(FeedEntry.COLUMN_NAME_LOCATION_KEY));
            long user_ID = cursorFeed.getInt(cursorFeed.getColumnIndex(FeedEntry.COLUMN_NAME_USER_KEY));

            // using the id's get location and user cursors
            Cursor cursorLocation = dataHandler.returnLocationData(location_ID);
            Cursor cursorUser = dataHandler.returnUserData(user_ID);

            // create location object
            cursorLocation.moveToFirst();
            Location location = new Location(
                    cursorLocation.getDouble(cursorLocation.getColumnIndex(LocationEntry.COLUMN_NAME_LATITUDE)),
                    cursorLocation.getDouble(cursorLocation.getColumnIndex(LocationEntry.COLUMN_NAME_LONGITUDE)));

            location.setObjectId(cursorLocation.getString(
                    cursorLocation.getColumnIndex(LocationEntry.COLUMN_NAME_OBJECT_ID)));

            location.setName(cursorLocation.getString(
                    cursorLocation.getColumnIndex(LocationEntry.COLUMN_NAME_NAME)));
            cursorLocation.close();

            // create user object
            cursorUser.moveToFirst();
            User user = new User(
                    cursorUser.getString(cursorUser.getColumnIndex(UserEntry.COLUMN_NAME_OBJECT_ID)),
                    cursorUser.getString(cursorUser.getColumnIndex(UserEntry.COLUMN_NAME_USERNAME)),
                    cursorUser.getString(cursorUser.getColumnIndex(UserEntry.COLUMN_NAME_FULL_NAME)));
            cursorUser.close();

            // create feed object
            Feed feed = new Feed(location, user, null);

            feed.setObjectId(cursorFeed.getString(cursorFeed.getColumnIndex(FeedEntry.COLUMN_NAME_OBJECT_ID)));
            feed.setCreatedAt(cursorFeed.getString(cursorFeed.getColumnIndex(FeedEntry.COLUMN_NAME_CREATED_AT)));
            feed.setMediaURI(cursorFeed.getString(cursorFeed.getColumnIndex(FeedEntry.COLUMN_NAME_MEDIA_URI)));
            feed.setTags(cursorFeed.getString(cursorFeed.getColumnIndex(FeedEntry.COLUMN_NAME_TAGS)));

            feedList.add(feed);
        }

        cursorFeed.close();
        dataHandler.close();

        return feedList;
    }




//
//    /**
//     * Executes the AsyncTask to update user relationship arrays on parse
//     *
//     * @param instance instance of interface that received the callback data
//     * @param objectId objectId of user current user clicked on
//     * @param status   current tag associated with the button (FOLLOW / UNFOLLOW)
//     */
//    public void updateUserRelationships(
//            UpdateUserRelationshipsResponse instance, String objectId, String status) {
//
//        UpdateUserRelationshipsAsyncTask updateUserRelationships = new UpdateUserRelationshipsAsyncTask();
//        updateUserRelationships.delegate = instance;
//        updateUserRelationships.execute(objectId, status);
//    }

}