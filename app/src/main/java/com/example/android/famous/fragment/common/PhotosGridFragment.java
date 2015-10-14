package com.example.android.famous.fragment.common;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.famous.R;
import com.example.android.famous.adapter.PhotosGridRecyclerViewAdapter;
import com.example.android.famous.adapter.PostFeedRecyclerViewAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosGridFragment extends Fragment {

    RecyclerView photosGridRecyclerView;
    PhotosGridRecyclerViewAdapter photosGridRecyclerViewAdapter;
    List<Integer> gridData;

    public PhotosGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos_grid, container, false);

        photosGridRecyclerView = (RecyclerView) view.findViewById(R.id.photosGridItemContainer);
        photosGridRecyclerViewAdapter = new PhotosGridRecyclerViewAdapter(getActivity(), gridData);
        photosGridRecyclerView.setAdapter(photosGridRecyclerViewAdapter);
        photosGridRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return view;
    }

    public List<Integer> getGridData() {
        return gridData;
    }

    public void setGridData(List<Integer> gridData) {
        this.gridData = gridData;
    }
}
