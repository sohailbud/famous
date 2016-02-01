package com.example.android.famous.fragment.camera;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.android.famous.R;
import com.example.android.famous.activity.EditMediaActivity;
import com.example.android.famous.activity.PhotoActivity;
import com.example.android.famous.camera.CameraPreviewHolder;
import com.example.android.famous.camera.FocusView;
import com.example.android.famous.camera.PreviewSurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static final String PICTURE_TAKEN_PATH = "picture_taken_path";

    private PreviewSurfaceView previewSurfaceView;
    private CameraPreviewHolder cameraPreviewHolder;
    private FocusView focusView;
    private Camera camera;

    // set default camera to
    private int cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;

    private ImageView captureButton;
    private ImageView cameraSwitchButton;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        previewSurfaceView = (PreviewSurfaceView) view.findViewById(R.id.preview_surface_view);

        focusView = (FocusView) view.findViewById(R.id.drawing_focus_view);

        captureButton = (ImageView) view.findViewById(R.id.capture_button);

        cameraSwitchButton = (ImageView) view.findViewById(R.id.camera_switch_button);
//        cameraSwitchButton.setOnClickListener(switchCameraButtonOnClickListener);


        ViewTreeObserver viewTreeObserver = previewSurfaceView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    cameraPreviewHolder.setDisplayWidth(previewSurfaceView.getWidth());
                    cameraPreviewHolder.setDisplayHeight(previewSurfaceView.getHeight());
                }
            });
        }

        // Adding a toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_grey_500_24dp);
            actionBar.setTitle(null);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        previewSurfaceView.setVisibility(View.VISIBLE);
        initializeCamera(cameraID);
    }

    @Override
    public void onPause() {
        super.onPause();

        releaseCamera();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.home):
                getActivity().finish();
        }
        return true;
    }

    /**
     * Create a file Uri for saving an image or video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    public File getOutputMediaFile(int type) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), this.getString(R.string.app_name));
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }

            // Create a media file name
//            String timeCreated = SimpleDateFormat.getDateTimeInstance().format(new Date());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
            String timeCreated = simpleDateFormat.format(Calendar.getInstance().getTime());

            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_" + timeCreated + ".jpg");

                Log.i("PATH", mediaFile.getPath());

            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_" + timeCreated + ".mp4");

            } else return null;

            return mediaFile;

        } else return null;
    }

    private Camera.PictureCallback picture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) return;

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                // start edit media activity
                Intent intent = new Intent(getActivity(), EditMediaActivity.class);
                intent.putExtra(PICTURE_TAKEN_PATH, pictureFile.getAbsolutePath());
                startActivity(intent);

            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }
        }
    };

    private View.OnClickListener captureButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            camera.takePicture(null, null, picture);
        }
    };

    private View.OnClickListener switchCameraButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cameraID == Camera.CameraInfo.CAMERA_FACING_BACK)
                cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
            else if (cameraID == Camera.CameraInfo.CAMERA_FACING_FRONT)
                cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;

            if (camera == null) {
                initializeCamera(cameraID);
            } else {
                releaseCamera();
                initializeCamera(cameraID);
            }
        }
    };

    /**
     * Releases camera
     */
    public void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            previewSurfaceView.setVisibility(View.GONE);
            previewSurfaceView.getHolder().removeCallback(cameraPreviewHolder);
            camera.release();
            camera = null;

        }
    }

    /**
     * Initialize camera
     */
    private void initializeCamera(int cameraID) {
        if (camera != null) return;

        // create instance of camera
        if (deviceHasCameraHardware(getActivity())) camera = getCameraInstance(cameraID);

        // if only one camera then hide switch camera button
        if (Camera.getNumberOfCameras() < 2) cameraSwitchButton.setVisibility(View.GONE);

        // display correct icon based on the cameraID
        switch (cameraID) {
            case Camera.CameraInfo.CAMERA_FACING_FRONT:
                cameraSwitchButton.setImageResource(R.drawable.ic_camera_front_white_36dp);
                break;
            case Camera.CameraInfo.CAMERA_FACING_BACK:
                cameraSwitchButton.setImageResource(R.drawable.ic_camera_rear_white_36dp);
                break;
        }

        // Create our Preview view
        cameraPreviewHolder = new CameraPreviewHolder(camera);

        // set display orientation
        setCameraDisplayOrientation((PhotoActivity) getActivity(), cameraID, camera);

        SurfaceHolder surfaceHolder = previewSurfaceView.getHolder();

        // Install a SurfaceHolder.Callback so we get notified
        // when underlying surface gets created and destroyed
        surfaceHolder.addCallback(cameraPreviewHolder);

        previewSurfaceView.setListener(cameraPreviewHolder);
        previewSurfaceView.setDrawingFocusView(focusView);

        captureButton.setOnClickListener(captureButtonOnClickListener);


    }

    /**
     * method provided by google docs
     */
    @SuppressWarnings("deprecation")
    private void setCameraDisplayOrientation(
            AppCompatActivity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  //compensate the mirror
        } else {    // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * Get an instance of camera object
     */
    @SuppressWarnings("deprecation")
    private Camera getCameraInstance(int cameraID) {
        Camera c = null;
        try {
            c = Camera.open(cameraID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * Check if phone has a camera
     */
    private boolean deviceHasCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}