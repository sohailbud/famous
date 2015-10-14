package com.example.android.famous.fragment.common;


import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.famous.R;
import com.example.android.famous.adapter.PostFeedRecyclerViewAdapter;
import com.example.android.famous.adapter.UserListRecyclerViewAdapter;
import com.example.android.famous.model.User;
import com.example.android.famous.util.DividerItemDecoration;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment {

    private List<User> userData;
    private RecyclerView userListRecyclerView;
    private UserListRecyclerViewAdapter userListRecyclerViewAdapter;
    private View view;

    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_list, container, false);

        final LinearLayoutManager linearLayoutManager = new
                com.example.android.famous.util.LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);

        userListRecyclerView = (RecyclerView) view.findViewById(R.id.userListItemContainer);
        userListRecyclerViewAdapter = new UserListRecyclerViewAdapter(getActivity(), userData);
        userListRecyclerView.setAdapter(userListRecyclerViewAdapter);
        userListRecyclerView.setLayoutManager(linearLayoutManager);
        userListRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));

        return view;
    }

    public List<User> getUserData() {
        return userData;
    }

    public void setUserData(List<User> userData) {
        this.userData = userData;
    }


}
