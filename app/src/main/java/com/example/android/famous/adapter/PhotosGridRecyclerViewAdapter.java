package com.example.android.famous.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.famous.R;

import java.util.List;

/**
 * Created by Sohail on 9/27/15.
 */
public class PhotosGridRecyclerViewAdapter extends RecyclerView.Adapter<PhotosGridRecyclerViewAdapter.MyViewHolder> {

    LayoutInflater inflater;
    List<Integer> data;

    public PhotosGridRecyclerViewAdapter(Context context, List<Integer> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_photos_grid_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public int getItemCount() {
        if (data != null) return data.size();
        else return 0;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.gridImage.setImageResource(data.get(position));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView gridImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            gridImage = (ImageView) itemView.findViewById(R.id.gridImage);
        }
    }
}
