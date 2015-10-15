package com.example.android.famous.callback;

import com.example.android.famous.model.Feed;

import java.util.List;

/**
 * Created by Sohail on 10/14/15.
 */
public interface FeedDataListener {

    void feedDataOnProcess(List<Feed> data);
}
