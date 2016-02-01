package com.example.android.famous.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.android.famous.interactor.PostInteractor;
import com.example.android.famous.model.Location;
import com.example.android.famous.util.ImageCache;
import com.example.android.famous.util.ImageLoader;

import java.io.ByteArrayOutputStream;

/**
 * Created by Sohail on 1/13/16.
 */
public class PostPresenter {

    private static PostPresenter postPresenter = null;

    /**
     * if {@link PostPresenter} instance exists, return that, otherwise create a new instance
     */
    public static PostPresenter getInstance() {
        if (postPresenter == null) postPresenter = new PostPresenter();
        return postPresenter;
    }

    private PostPresenter() {
    }

    public void postToParse(String path, Context context) {
        Bitmap bitmap = ImageCache.getInstance().getBitmapFromDiskCache(path, context);
        bitmap = resizeBitmap(bitmap);

        if (bitmap != null)
            PostInteractor.getInstance(). new PostObjectTask().execute(bitmap, new Location(88, 88));
    }

    public Bitmap resizeBitmap(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();
        Bitmap newBitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);

        int newSize = newBitmap.getRowBytes() * newBitmap.getHeight();
        int size = bitmap.getRowBytes() * bitmap.getHeight();

        return newBitmap;

    }


}
