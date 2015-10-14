package com.example.android.famous.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.android.famous.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    android.support.v7.app.ActionBar ab;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = layoutInflater.inflate(R.layout.search, null);
//        ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(false);
//        ab.setDisplayShowCustomEnabled(true);
//        ab.setDisplayShowTitleEnabled(false);
//        ab.setCustomView(v);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        final View activityRootView = getActivity().findViewById(R.id.activityRoot);
//        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
//                if (heightDiff > 500) {
//                    ab.setDisplayHomeAsUpEnabled(true);
//                } else {
//                    ab.setDisplayHomeAsUpEnabled(false);
//                }
//            }
//        });


    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    Log.i("CHECKED", "CLICKED");
//
//                    return false;
//                }
//            });
//        }
//        return false;
//    }


}








