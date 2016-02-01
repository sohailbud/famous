package com.example.android.famous.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.android.famous.activity.EditMediaActivity;
import com.fenchtose.nocropper.CropperView;

import java.io.ByteArrayOutputStream;

/**
 * Created by Sohail on 1/11/16.
 */
public class ImageCropper {

    private Bitmap bitmap;
    private boolean isSnappedToCenter = false;
    private static final int ROTATE_DEGREE = 90;
    private Context context;
    private CropperView imageView;
    public static final String CROPPED_IMAGE_PATH = "cropped_image_path";


    public ImageCropper(
            ImageView rotateButton, ImageView snapButton, CropperView imageView, Context context) {

        this.imageView = imageView;
        this.context = context;

        snapButton.setOnClickListener(snapButtonOnClickListener);
        rotateButton.setOnClickListener(rotateButtonOnClickListener);
    }

    private View.OnClickListener snapButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            snapBitmap();
        }
    };

    private View.OnClickListener rotateButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rotateBitmap();
            imageView.setImageBitmap(bitmap);
            imageView.invalidate();
        }
    };

    private void rotateBitmap() {
        if (bitmap == null) return;

        Matrix matrix = new Matrix();
        matrix.postRotate(ROTATE_DEGREE);
        bitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void snapBitmap() {
        if (isSnappedToCenter) {
            imageView.cropToCenter();
            imageView.invalidate();
        } else {
            imageView.fitToCenter();
            imageView.invalidate();
        }

        isSnappedToCenter = !isSnappedToCenter;
    }

    public void loadNewImage(String filePath) {
        bitmap = BitmapFactory.decodeFile(filePath);

        if (bitmap == null) return;

        int maxP = Math.max(bitmap.getWidth(), bitmap.getHeight());
        float scale1280 = (float) maxP / 1280;

        if (imageView.getWidth() != 0) {
            imageView.setMaxZoom(imageView.getWidth() * 2 / 1280f);
        } else {

            ViewTreeObserver vto = imageView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    imageView.setMaxZoom(imageView.getWidth() * 2 / 1280f);
                    return true;
                }
            });

        }

        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / scale1280),
                (int) (bitmap.getHeight() / scale1280), true);
        imageView.setImageBitmap(bitmap);
        imageView.setTag(filePath);

        isSnappedToCenter = false;
    }

    public String saveCroppedImage() {

        String croppedImagePath =
                CROPPED_IMAGE_PATH + "_" + Uri.parse((String) imageView.getTag()).getLastPathSegment();

        Bitmap croppedBitmap = imageView.getCroppedBitmap();
//        int size = croppedBitmap.getRowBytes() * croppedBitmap.getHeight();

        // RESIZE IMAGE



//        int newSize = bitmap.getRowBytes() * bitmap.getHeight();


        ImageCache.getInstance().addBitmapToDiskCache(
                croppedBitmap, context, croppedImagePath);

        return croppedImagePath;
    }

    public void cropImage() {
        Intent intent = new Intent(context, EditMediaActivity.class);
        intent.putExtra(CROPPED_IMAGE_PATH, saveCroppedImage());
        context.startActivity(intent);
    }
}
