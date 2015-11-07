package com.example.android.famous.presenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.example.android.famous.activity.MainActivity;
import com.example.android.famous.adapter.FeedRecyclerViewAdapter;
import com.example.android.famous.callback.FeedFragmentInterface;
import com.example.android.famous.fragment.HomeFragment;
import com.example.android.famous.fragment.common.FeedFragment;
import com.example.android.famous.interactor.FeedDataInteractor;
import com.example.android.famous.model.DataHandler;
import com.example.android.famous.model.Feed;
import com.example.android.famous.model.Location;
import com.example.android.famous.model.User;

import java.util.ArrayList;
import java.util.List;


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

    @Override
    public void feedDataOnProcess(List<Feed> feedList) {
        if (feedRecyclerViewAdapter != null) feedRecyclerViewAdapter.newDataInsert(feedList);
        insertInDatabase(feedList);

    }

    public void insertInDatabase(List<Feed> feedList) {

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

    public void fetchDataForAdapter(FeedRecyclerViewAdapter feedRecyclerViewAdapter, FeedFragment feeedFragment) {
        // get data from server
        FeedDataInteractor.GetFeedDataTask feedDataTask = new FeedDataInteractor(). new GetFeedDataTask();
        feedDataTask.delegate = this;
        feedDataTask.execute();
        this.feedRecyclerViewAdapter = feedRecyclerViewAdapter;
        this.feedFragment = feeedFragment;

        // get current sql data
        List<Feed> feedList = getSqlFeedData();
        feedRecyclerViewAdapter.newDataInsert(feedList);

    }

    public List<Feed> getSqlFeedData() {
        DataHandler dataHandler = new DataHandler(feedFragment.getActivity());
        Cursor cursorFeed = dataHandler.returnFeedData();

        List<Feed> feedList = new ArrayList<>();

        while (cursorFeed.moveToNext()) {

            long location_ID = cursorFeed.getInt(4);
            long user_ID = cursorFeed.getInt(5);

            Cursor cursorLocation = dataHandler.returnLocationData(location_ID);
            Cursor cursorUser = dataHandler.returnUserData(user_ID);

            cursorLocation.moveToFirst();
            Location location = new Location(cursorLocation.getDouble(1), cursorLocation.getDouble(2));
            location.setObjectId(cursorLocation.getString(0));
            location.setName(cursorLocation.getString(3));
            cursorLocation.close();

            cursorUser.moveToFirst();
            User user = new User(cursorUser.getString(0), cursorUser.getString(1), cursorUser.getString(2));
            cursorUser.close();

            Feed feed = new Feed(location, user, null);

            feed.setObjectId(cursorFeed.getString(0));
            feed.setCreatedAt(cursorFeed.getString(1));
            feed.setMediaURI(cursorFeed.getString(2));
            feed.setTags(cursorFeed.getString(3));

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