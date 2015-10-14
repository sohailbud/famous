package com.example.android.famous.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.famous.R;
import com.example.android.famous.fragment.common.UserPlusThreePostsFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowPeopleFragment extends Fragment {


    public FollowPeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_follow_people, container, false);

        getChildFragmentManager().beginTransaction().add(
                R.id.fragmentFollowPeopleUserPlusThreePostsContainer,
                new UserPlusThreePostsFragment()).commit();

        return view;
    }


}
