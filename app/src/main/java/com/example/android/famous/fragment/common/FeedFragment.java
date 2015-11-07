package com.example.android.famous.fragment.common;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.famous.R;
import com.example.android.famous.adapter.FeedRecyclerViewAdapter;
import com.example.android.famous.callback.FeedFragmentInterface;
import com.example.android.famous.model.Feed;
import com.example.android.famous.presenter.HomeFragmentPresenter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    RecyclerView postFeedRecyclerView;
    FeedRecyclerViewAdapter postFeedRecyclerViewAdapter;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        postFeedRecyclerView = (RecyclerView) view.findViewById(R.id.postFeedItemContainer);
        postFeedRecyclerViewAdapter = new FeedRecyclerViewAdapter(getActivity());
        postFeedRecyclerView.setAdapter(postFeedRecyclerViewAdapter);
        postFeedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        HomeFragmentPresenter homeFragmentPresenter = HomeFragmentPresenter.getInstance();
        homeFragmentPresenter.fetchDataForAdapter(postFeedRecyclerViewAdapter, this);

        return view;
    }

}