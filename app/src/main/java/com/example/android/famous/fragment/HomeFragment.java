package com.example.android.famous.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.famous.R;
import com.example.android.famous.callback.FeedDataListener;
import com.example.android.famous.fragment.common.FeedFragment;
import com.example.android.famous.fragment.common.UserListFragment;
import com.example.android.famous.model.Feed;
import com.example.android.famous.model.User;
import com.example.android.famous.presenter.HomeFragmentPresenter;
import com.example.android.famous.callback.SuggestedUserDataListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements SuggestedUserDataListener {

    // presenter instance
    HomeFragmentPresenter homeFragmentPresenter = HomeFragmentPresenter.getInstance();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // add suggestions fragment and pass data
//        homeFragmentPresenter.getSuggestedUserData(this);

        // add feed fragment and pass data
//        homeFragmentPresenter.getFeedData(this);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void suggestedUserDataOnProcess(List<User> data) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        UserListFragment userListFragment = new UserListFragment();
        userListFragment.setUserData(data);
        fragmentTransaction.add(R.id.fragmentHomeSuggestionsFeedContainer, userListFragment);
        fragmentTransaction.commit();

    }



}