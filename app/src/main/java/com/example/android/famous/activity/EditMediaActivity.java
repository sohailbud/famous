package com.example.android.famous.activity;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.famous.R;
import com.example.android.famous.fragment.camera.CameraFragment;
import com.example.android.famous.fragment.camera.CropperFragment;
import com.example.android.famous.fragment.camera.EditMediaFragment;
import com.example.android.famous.presenter.PostPresenter;
import com.example.android.famous.util.ImageCropper;

public class EditMediaActivity extends AppCompatActivity
        implements EditMediaFragment.OnPostImageCallback {

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_media);

        String croppedImagePath = getIntent().getStringExtra(ImageCropper.CROPPED_IMAGE_PATH);
        String pictureTakenPath = getIntent().getStringExtra(CameraFragment.PICTURE_TAKEN_PATH);

        Bundle args = new Bundle();

        if (croppedImagePath != null) {
            args.putString(ImageCropper.CROPPED_IMAGE_PATH, croppedImagePath);
            EditMediaFragment editMediaFragment = new EditMediaFragment();
            editMediaFragment.onPostImageListener = this;
            editMediaFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.edit_media_fragment_container,
                    editMediaFragment).commit();

        } else if (pictureTakenPath != null) {
            args.putString(CameraFragment.PICTURE_TAKEN_PATH, pictureTakenPath);
            CropperFragment cropperFragment = new CropperFragment();
            cropperFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.edit_media_fragment_container,
                    cropperFragment).commit();
        }
    }

    @Override
    public void onPostImageCallback(String path) {
        PostPresenter.getInstance().postToParse(path, this);

        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("GLOBAL", TAG + "_DESTROYED");
    }

}
