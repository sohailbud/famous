package com.example.android.famous.fragment.camera;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.android.famous.adapter.GalleryRecyclerViewAdapter;
import com.example.android.famous.R;

import java.util.ArrayList;

/**
 * This class is a subview of {@link GalleryFragment}
 * Purpose of this class is to load a grid of images using {@link RecyclerView}
 * It implements {@link GalleryRecyclerViewAdapter.ImageClickCallBack} to listen for clicks and inform
 * parent {@link GalleryFragment} fragment so parent fragment can successfully display the images in cropperView
 */
public class GalleryImagePickerFragment extends Fragment
        implements GalleryRecyclerViewAdapter.ImageClickCallBack {

    // listens for clicks on images
    public ImageClickCallBack imageClickListener = null;

    // key to get list of image path data from parent fragment
    private static final String LIST_IMAGES_KEY = "listImagesKey";

    public GalleryImagePickerFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_image_picker, container, false);

        // recycler view
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.photosGridItemContainer);

        // gets list of images path from parent fragment
        ArrayList<String> data = getArguments().getStringArrayList(LIST_IMAGES_KEY);

        // create an adapter and set data
        final GalleryRecyclerViewAdapter galleryRecyclerViewAdapter = new GalleryRecyclerViewAdapter(
                getActivity(), data);

        // set the listener to hear image clicks
        galleryRecyclerViewAdapter.imageClickListener = this;

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a grid layout manager
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        // specify an adapter
        recyclerView.setAdapter(galleryRecyclerViewAdapter);

        // subject to change BELOW
        final TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        ViewTreeObserver tabLayoutViewTreeObserver = tabLayout.getViewTreeObserver();

        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerView.setPadding(0, 0, 0, tabLayout.getHeight());

            }
        };

        tabLayoutViewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);

        if (Build.VERSION.SDK_INT >= 16)
            tabLayoutViewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        // subject to change ABOVE

        return view;
    }

    /**
     * listens for image clicks from adapter
     */
    @Override
    public void imageClickedCallBack(String path) {
        imageClickListener.imageClickedCallBack(path);
    }

    /**
     * informs parent fragment about image clicks
     */
    public interface ImageClickCallBack {
        void imageClickedCallBack(String path);
    }
}