package com.example.android.famous.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.famous.R;
import com.example.android.famous.model.User;

import java.util.List;

/**
 * Created by Sohail on 9/25/15.
 */
public class UserPlusThreePostsRecyclerViewAdapter extends
        RecyclerView.Adapter<UserPlusThreePostsRecyclerViewAdapter.MyViewHolder> {

    LayoutInflater inflater;
    List<User> data;

    public UserPlusThreePostsRecyclerViewAdapter(Context context, List<User> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getItemCount() {
        if (data != null) return data.size();
        else return 0;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView userName;
        TextView fullName;
        TextView bio;
        ImageView image1;
        ImageView image2;
        ImageView image3;

        public MyViewHolder(View itemView) {
            super(itemView);

            avatar = (ImageView) itemView.findViewById(R.id.userListAvatar);
            userName = (TextView) itemView.findViewById(R.id.userListUserName);
            fullName = (TextView) itemView.findViewById(R.id.userListFullName);
            bio = (TextView) itemView.findViewById(R.id.userPlusThreePostsBio);
            image1 = (ImageView) itemView.findViewById(R.id.userPlusThreePostsItemImage1);
            image2 = (ImageView) itemView.findViewById(R.id.userPlusThreePostsItemImage2);
            image3 = (ImageView) itemView.findViewById(R.id.userPlusThreePostsItemImage3);
        }
    }
}
