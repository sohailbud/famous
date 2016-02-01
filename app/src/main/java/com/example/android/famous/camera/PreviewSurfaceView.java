package com.example.android.famous.camera;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by Sohail on 12/3/15.
 */
public class PreviewSurfaceView extends SurfaceView {

    private CameraPreviewHolder cameraPreviewHolder;

    @SuppressWarnings("deprecation")
    private List<Camera.Size> supportedPreviewSizes;

    private static  final int FOCUS_AREA_SIZE= 100;

    @SuppressWarnings("deprecation")
    private Camera.Size previewSize;

    private boolean listenerSet = false;
    private boolean focusViewSet = false;
    private FocusView focusView;

    public PreviewSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        setMeasuredDimension(width, height);

        Camera camera = cameraPreviewHolder.getCamera();
        if (camera != null)
            supportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();

        if (supportedPreviewSizes != null)
            previewSize = getOptimalPreviewSize(supportedPreviewSizes, width, height);

        cameraPreviewHolder.setPreviewSize(previewSize);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!listenerSet) return false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Rect[] focusRect = calculateFocusArea(event);

            cameraPreviewHolder.onTouchFocus(focusRect[1]);
            if (focusViewSet) {
                focusView.onTouch(true, focusRect[0]);
                focusView.invalidate();

                // Remove the square after sometime
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        focusView.onTouch(false, new Rect(0, 0, 0, 0));
                        focusView.invalidate();
                    }
                }, 1000);
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * set CameraPreviewHolder instance for touch focus.
     */
    public void setListener(CameraPreviewHolder cameraPreviewHolder) {
        this.cameraPreviewHolder = cameraPreviewHolder;
        listenerSet = true;
    }

    /**
     * set DrawingView instance for touch focus indication.
     */
    public void setDrawingFocusView(FocusView focusView) {
        this.focusView = focusView;
        focusViewSet = true;
    }

    /**
     * /Convert from View's width and height to +/- 1000
     */
    private Rect[] calculateFocusArea(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        if(x + FOCUS_AREA_SIZE > cameraPreviewHolder.getDisplayWidth())
            x = cameraPreviewHolder.getDisplayWidth() - FOCUS_AREA_SIZE;

        if(x - FOCUS_AREA_SIZE < 0) x = FOCUS_AREA_SIZE;

        Rect focusRectDraw = new Rect(
               (int) (x - FOCUS_AREA_SIZE),
               (int) (y - FOCUS_AREA_SIZE),
               (int) (x + FOCUS_AREA_SIZE),
               (int) (y + FOCUS_AREA_SIZE));

        Rect focusRect = new Rect(
                focusRectDraw.left * 2000 / cameraPreviewHolder.getDisplayWidth() - 1000,
                focusRectDraw.top * 2000 / cameraPreviewHolder.getDisplayHeight() - 1000,
                focusRectDraw.right * 2000 / cameraPreviewHolder.getDisplayWidth() - 1000,
                focusRectDraw.bottom * 2000 / cameraPreviewHolder.getDisplayHeight() - 1000);

        Matrix matrix = this.getMatrix();
        matrix.postRotate(90);


        return new Rect[] {focusRectDraw, focusRect};
    }


}