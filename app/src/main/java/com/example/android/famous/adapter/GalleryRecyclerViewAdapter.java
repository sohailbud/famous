package com.example.android.famous.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.famous.R;
import com.example.android.famous.util.ImageLoader;

import java.util.List;

/**
 * Created by Sohail on 12/5/15.
 */
public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<String> data;

    // loads the image
    private ImageLoader imageLoader;

    // listens for image clicks
    public ImageClickCallBack imageClickListener = null;

    public GalleryRecyclerViewAdapter(Context context, List<String> data) {
        inflater = LayoutInflater.from(context);
        this.imageLoader = new ImageLoader(context, false);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_gallery_image_picker_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Sets the path of the image as a tag and calls on {@link ImageLoader} class to display the image
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.gridImage.setTag(data.get(position));
        imageLoader.displayImage(data.get(position), holder.gridImage);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView gridImage;

        public ViewHolder(View itemView) {
            super(itemView);

            gridImage = (ImageView) itemView.findViewById(R.id.gridImage);
            gridImage.setOnClickListener(this);

        }

        /**
         * When an image is clicked, the callback method is called to inform any listeners
         */
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
            imageClickListener.imageClickedCallBack(v.getTag().toString());

        }
    }

    /**
     * A callback interface, implemented by
     * {@link com.example.android.famous.fragment.camera.GalleryImagePickerFragment}
     */
    public interface ImageClickCallBack {
        void imageClickedCallBack(String path);
    }
}

