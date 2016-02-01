package com.example.android.famous.fragment.camera;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.famous.R;
import com.example.android.famous.util.ImageCache;
import com.example.android.famous.util.ImageCropper;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class EditMediaFragment extends Fragment {

    public OnPostImageCallback onPostImageListener;
//    private String croppedImagePath;

    public EditMediaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_media, container, false);

        final String croppedImagePath = getArguments().getString(ImageCropper.CROPPED_IMAGE_PATH);

        ImageView imageView = (ImageView) view.findViewById(R.id.editable_media);

        Bitmap bitmap = ImageCache.getInstance().getBitmapFromDiskCache(croppedImagePath, getActivity());

        imageView.setImageBitmap(bitmap);

        view.findViewById(R.id.post_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostImageListener.onPostImageCallback(croppedImagePath);
            }
        });

        return view;
    }

//    private View.OnClickListener onPostButtonClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            onPostImageListener.onPostImageCallback(croppedImagePath);
//        }
//    };

    public interface OnPostImageCallback {
        void onPostImageCallback(String path);
    }
}
