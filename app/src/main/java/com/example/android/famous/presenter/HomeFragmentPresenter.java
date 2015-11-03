package com.example.android.famous.presenter;

import com.example.android.famous.adapter.FeedRecyclerViewAdapter;
import com.example.android.famous.callback.FeedFragmentInterface;
import com.example.android.famous.interactor.FeedDataInteractor;
import com.example.android.famous.model.Feed;

import java.util.List;


/**
 * Created by Sohail on 9/24/15.
 */
public class HomeFragmentPresenter implements FeedFragmentInterface {

    private static HomeFragmentPresenter homeFragmentPresenter = null;
    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;

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
        if (feedRecyclerViewAdapter != null) feedRecyclerViewAdapter.swap(feedList);

    }

    public void fetchDataForAdapter(FeedRecyclerViewAdapter feedRecyclerViewAdapter) {
        FeedDataInteractor.GetFeedDataTask feedDataTask = new FeedDataInteractor(). new GetFeedDataTask();
        feedDataTask.execute();
        this.feedRecyclerViewAdapter = feedRecyclerViewAdapter;

        // get current sql data
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