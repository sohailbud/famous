package com.example.android.famous.fragment.camera;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.famous.R;
import com.example.android.famous.util.ImageCropper;
import com.fenchtose.nocropper.CropperView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements GalleryImagePickerFragment.ImageClickCallBack {

    private static final String LIST_IMAGES_KEY = "listImagesKey";

    public static String firstImage = null;

    private ImageCropper imageCropper;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

//        imageView = (CropperView) view.findViewById(R.id.cropper_view);
//        view.findViewById(R.id.snap_button).setOnClickListener(snapButtonOnClickListener);
//        view.findViewById(R.id.rotate_button).setOnClickListener(rotateButtonOnClickListener);
        imageCropper = new ImageCropper(
                (ImageView) view.findViewById(R.id.rotate_button),
                (ImageView) view.findViewById(R.id.snap_button),
                (CropperView) view.findViewById(R.id.cropper_view),
                getActivity());

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_grey_500_24dp);
            actionBar.setTitle(null);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle args = new Bundle();
        args.putStringArrayList(LIST_IMAGES_KEY, getFullImages(getActivity()));

        GalleryImagePickerFragment galleryImagePickerFragment = new GalleryImagePickerFragment();
        galleryImagePickerFragment.imageClickListener = this;
        galleryImagePickerFragment.setArguments(args);

        getChildFragmentManager().beginTransaction().add(
                R.id.gallery_image_picker_container,
                galleryImagePickerFragment).commit();

        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.cropper_view_app_bar);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });

        params.setBehavior(behavior);


        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.check_action):
                imageCropper.cropImage();
                break;
            case (R.id.home):
                getActivity().finish();
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_camera, menu);
    }

    @Override
    public void imageClickedCallBack(String path) {
        imageCropper.loadNewImage(path);
    }

    /**
     * gets images from phone and stores the paths in a list
     */
    private ArrayList<String> getFullImages(Context context) {

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String order = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        CursorLoader imagesCursorLoader = new CursorLoader(
                context, uri, null, null, null, order);
        Cursor imageCursor = imagesCursorLoader.loadInBackground();

        ArrayList<String> imagesData = new ArrayList<>();
        while (imageCursor.moveToNext()) {
            int dataIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String data = imageCursor.getString(dataIndex);
            imagesData.add(data);
        }
        imageCursor.close();

        firstImage = imagesData.get(0);
        imageCropper.loadNewImage(firstImage);

        return imagesData;

    }
}