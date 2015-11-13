package com.example.android.famous.fragment.common;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.famous.R;
import com.example.android.famous.adapter.FeedRecyclerViewAdapter;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    RecyclerView feedRecyclerView;
    FeedRecyclerViewAdapter feedRecyclerViewAdapter;
    private OnCompleteListener mListener;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        feedRecyclerView = (RecyclerView) view.findViewById(R.id.postFeedItemContainer);
        feedRecyclerViewAdapter = new FeedRecyclerViewAdapter(getActivity());
        feedRecyclerView.setAdapter(feedRecyclerViewAdapter);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mListener != null) mListener.onComplete(feedRecyclerViewAdapter, this);

        return view;
    }

    public interface OnCompleteListener {
        void onComplete(
                FeedRecyclerViewAdapter feedRecyclerViewAdapter, FeedFragment feedFragment);
    }

    public void setmListener(OnCompleteListener mListener) {
        this.mListener = mListener;
    }

    public static void saveLogcatToFile(Context context) {
        String fileName = "logcat_"+System.currentTimeMillis()+".txt";
        File outputFile = new File(context.getExternalCacheDir(),fileName);
        try {
            @SuppressWarnings("unused")
            Process process = Runtime.getRuntime().exec("logcat -f "+outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}