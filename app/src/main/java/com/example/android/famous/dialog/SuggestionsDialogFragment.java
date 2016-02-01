package com.example.android.famous.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.famous.R;
import com.example.android.famous.adapter.UserListRecyclerViewAdapter;
import com.example.android.famous.fragment.common.UserListFragment;
import com.example.android.famous.model.User;
import com.example.android.famous.util.DividerItemDecoration;

import java.util.List;

/**
 * Created by Sohail on 1/12/16.
 */
public class SuggestionsDialogFragment extends DialogFragment
        implements UserListRecyclerViewAdapter.UserListItemClickedCallback {

    public OnUserListRecyclerViewAdapterAvailableCallback
            onUserListRecyclerViewAdapterAvailableListener;

    public UserListItemClickedCallback userListItemClickedListener;

    public static SuggestionsDialogFragment newInstance() {
        return new SuggestionsDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

//        final LinearLayoutManager linearLayoutManager = new
//                com.example.android.famous.util.LinearLayoutManager(
//                getActivity(), LinearLayoutManager.VERTICAL, false);
//        userListRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));


        RecyclerView userListRecyclerView = (RecyclerView) view.findViewById(R.id.userListItemContainer);
        UserListRecyclerViewAdapter userListRecyclerViewAdapter = new UserListRecyclerViewAdapter(getActivity());
        userListRecyclerView.setAdapter(userListRecyclerViewAdapter);
        userListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userListRecyclerViewAdapter.userListItemClickedListener = this;

        if (onUserListRecyclerViewAdapterAvailableListener != null)
            onUserListRecyclerViewAdapterAvailableListener.onUserListRecyclerViewAdapterAvailableCallback(
                    userListRecyclerViewAdapter);

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(getActivity().getResources().getString(R.string.suggestionsForYou));
        return dialog;
    }

    public interface OnUserListRecyclerViewAdapterAvailableCallback {
        void onUserListRecyclerViewAdapterAvailableCallback(
                UserListRecyclerViewAdapter userListRecyclerViewAdapter);
    }

    @Override
    public void UserListItemClickedCallback(String objectId, View view) {
        userListItemClickedListener.userListItemClickedCallback(objectId, view);

    }

    public interface UserListItemClickedCallback {
        void userListItemClickedCallback(String objectId, View view);
    }
}
