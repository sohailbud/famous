package com.example.android.famous.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.famous.R;
import com.example.android.famous.model.Feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sohail on 9/15/15.
 */
public class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedRecyclerViewAdapter.MyViewHolder> {

    LayoutInflater inflater;
    List<Feed> feedData = new ArrayList<>();

    public FeedRecyclerViewAdapter(Context context) {

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        if (feedData != null) return feedData.size();
        else return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_feed_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        if (!feedData.isEmpty()) {
            Feed currentData = feedData.get(position);
            viewHolder.postAvatar.setImageResource(currentData.getUser().getProfilePicture());
            viewHolder.postUserName.setText(currentData.getUser().getUsername());
            viewHolder.postTime.setText(currentData.getCreatedAt());
//            viewHolder.postImage.setImageBitmap(currentData.getMedia());
        }
    }

    public void swap(List<Feed> data) {
        feedData.clear();
        feedData.addAll(data);
        notifyDataSetChanged();
    }

    public void newDataInsert(List<Feed> data) {
        feedData.addAll(data);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView postAvatar;
        TextView postUserName;
        TextView postTime;
        ImageView postImage;
        ImageView postLike;
        TextView postLikes;

        public MyViewHolder(View itemView) {
            super(itemView);

            postAvatar = (ImageView) itemView.findViewById(R.id.postFeedAvatar);
            postUserName = (TextView) itemView.findViewById(R.id.postFeedUserName);
            postTime = (TextView) itemView.findViewById(R.id.postFeedTime);
            postImage = (ImageView) itemView.findViewById(R.id.postFeedImage);
            postLike = (ImageView) itemView.findViewById(R.id.postFeedLike);
            postLikes = (TextView) itemView.findViewById(R.id.postFeedLikes);
        }
    }
}