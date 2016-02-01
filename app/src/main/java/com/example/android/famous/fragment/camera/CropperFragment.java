package com.example.android.famous.fragment.camera;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.famous.R;
import com.example.android.famous.util.ImageCropper;
import com.fenchtose.nocropper.CropperView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CropperFragment extends Fragment {

    private ImageCropper imageCropper;

    public CropperFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cropper, container, false);

        final String PICTURE_TAKE_PATH = getArguments().getString(CameraFragment.PICTURE_TAKEN_PATH);

        imageCropper = new ImageCropper(
                (ImageView) view.findViewById(R.id.rotate_button),
                (ImageView) view.findViewById(R.id.snap_button),
                (CropperView) view.findViewById(R.id.cropper_view),
                getActivity());

        imageCropper.loadNewImage(PICTURE_TAKE_PATH);

        view.findViewById(R.id.crop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCropper.cropImage();
            }
        });


        return view;
    }



}
