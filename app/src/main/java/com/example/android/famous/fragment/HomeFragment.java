package com.example.android.famous.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.famous.R;
import com.example.android.famous.adapter.FeedRecyclerViewAdapter;
import com.example.android.famous.adapter.UserListRecyclerViewAdapter;
import com.example.android.famous.dialog.SuggestionsDialogFragment;
import com.example.android.famous.fragment.common.FeedFragment;
import com.example.android.famous.fragment.common.UserListFragment;
import com.example.android.famous.interactor.UserRelationshipInteractor;
import com.example.android.famous.model.User;
import com.example.android.famous.presenter.HomeFragmentPresenter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
        implements FeedFragment.OnFeedRecyclerViewAdapterAvailableCallback {

    // presenter instance
    private HomeFragmentPresenter homeFragmentPresenter = HomeFragmentPresenter.getInstance();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        FeedFragment feedFragment = new FeedFragment();
        feedFragment.onFeedRecyclerViewAdapterAvailableListener = this;
        fragmentTransaction.add(R.id.fragmentHomeFeedContainer, feedFragment);

        fragmentTransaction.commit();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onFeedRecyclerViewAdapterAvailableCallback(
            FeedRecyclerViewAdapter feedRecyclerViewAdapter, FeedFragment feedFragment) {
        homeFragmentPresenter.fetchDataForAdapter(feedRecyclerViewAdapter, feedFragment);
    }
}