package com.example.android.famous.camera;

import android.graphics.Rect;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 11/23/15.
 */
public class CameraPreviewHolder implements SurfaceHolder.Callback {

    private int displayWidth, displayHeight;

    @SuppressWarnings("deprecation")
    private Camera camera;

    @SuppressWarnings("deprecation")
    private Camera.Size previewSize;

    public CameraPreviewHolder(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (camera != null)
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (holder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop non-existent preview
        }

        // set preview size and make any resize, rotate or reformatting changes here
        Camera.Parameters cameraParams = camera.getParameters();

        if (previewSize != null) cameraParams.setPreviewSize(previewSize.width, previewSize.height);

        cameraParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        camera.setParameters(cameraParams);

        // start preview with new settings
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Takes care of releasing the camera preview in your activity.
//        releaseCamera();
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }

    /**
     * Releases camera
     */
    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    /**
     * Called from PreviewSurfaceView to set touch focus.
     */
    @SuppressWarnings("deprecation")
    public void onTouchFocus(Rect touchFocusRect) {

        List<Camera.Area> focusList = new ArrayList<>();
        Camera.Area focusArea = new Camera.Area(touchFocusRect, 1000);
        focusList.add(focusArea);

        Camera.Parameters cameraParams = camera.getParameters();
        cameraParams.setFocusAreas(focusList);
        cameraParams.setMeteringAreas(focusList);
        camera.setParameters(cameraParams);

        camera.autoFocus(autoFocusCallback);
    }

    /**
     * Autofocus Callback
     */
    @SuppressWarnings("deprecation")
    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) camera.cancelAutoFocus();
        }
    };

    /**
     * get instance of camera
     */
    public Camera getCamera() {
        return camera != null ? camera : null;
    }

    /**
     * set preview size from {@link PreviewSurfaceView#onMeasure(int, int)}
     */
    public void setPreviewSize(Camera.Size previewSize) {
        this.previewSize = previewSize;
    }
}


