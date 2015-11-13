package com.example.android.famous.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.famous.R;
import com.example.android.famous.adapter.FeedRecyclerViewAdapter;
import com.example.android.famous.fragment.common.FeedFragment;
import com.example.android.famous.presenter.HomeFragmentPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements FeedFragment.OnCompleteListener {

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
        feedFragment.setmListener(this);
        fragmentTransaction.add(R.id.fragmentHomeFeedContainer, feedFragment);
        fragmentTransaction.commit();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onComplete(FeedRecyclerViewAdapter feedRecyclerViewAdapter, FeedFragment feedFragment) {
        homeFragmentPresenter.fetchDataForAdapter(feedRecyclerViewAdapter, feedFragment);
    }

    //    @Override
//    public void suggestedUserDataOnProcess(List<User> data) {
//        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        UserListFragment userListFragment = new UserListFragment();
//        userListFragment.setUserData(data);
//        fragmentTransaction.add(R.id.fragmentHomeSuggestionsFeedContainer, userListFragment);
//        fragmentTransaction.commit();
//    }



}