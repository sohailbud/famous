package com.example.android.famous.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.famous.R;
import com.example.android.famous.callback.UpdateUserRelationshipsResponse;
import com.example.android.famous.model.User;
import com.example.android.famous.presenter.HomeFragmentPresenter;

import java.util.List;

/**
 * Created by Sohail on 9/15/15.
 */
public class UserListRecyclerViewAdapter
        extends RecyclerView.Adapter<UserListRecyclerViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<User> data;
    private HomeFragmentPresenter homeFragmentPresenter = HomeFragmentPresenter.getInstance();
    private Context context;

    public UserListRecyclerViewAdapter(Context context, List<User> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.fragment_user_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (data != null) return data.size();
        else return 0;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User currentUserData = data.get(position);
        String fullName = currentUserData.getFullName();

        holder.userListProfilePicture.setImageResource(currentUserData.getProfilePicture());
        holder.userListUserName.setText(currentUserData.getUsername());
        holder.userListFullName.setText(fullName);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, UpdateUserRelationshipsResponse {

        ImageView userListProfilePicture;
        TextView userListUserName;
        TextView userListFullName;
        TextView userListFollow;
        ImageView userListClear;

        public MyViewHolder(View itemView) {
            super(itemView);

            userListProfilePicture = (ImageView) itemView.findViewById(R.id.userListAvatar);
            userListUserName = (TextView) itemView.findViewById(R.id.userListUserName);
            userListFullName = (TextView) itemView.findViewById(R.id.userListFullName);
            userListFollow = (Button) itemView.findViewById(R.id.userListFollow);
            userListClear = (ImageView) itemView.findViewById(R.id.userListClear);

            userListProfilePicture.setOnClickListener(this);
            userListUserName.setOnClickListener(this);
            userListFullName.setOnClickListener(this);
            userListFollow.setOnClickListener(this);
            userListClear.setOnClickListener(this);
        }

        @Override
        public void userRelationshipsUpdated(boolean successfullyUpdated, String status) {

            final String FOLLOW = "FOLLOW";
            final String UN_FOLLOW = "UN_FOLLOW";

            String follow = context.getResources().getString(R.string.follow);
            String following = context.getResources().getString(R.string.following);

            if (successfullyUpdated && status.equals(UN_FOLLOW)) {
                userListFollow.setText(following);
                userListFollow.setTag(UN_FOLLOW);
            }
            else if (successfullyUpdated && status.equals(FOLLOW)) {
                userListFollow.setText(follow);
                userListFollow.setTag(FOLLOW);
            }
            userListFollow.setClickable(true);
        }

        @Override
        public void onClick(View v) {

            int position = -1;
            if (getAdapterPosition() == getLayoutPosition()) position = getAdapterPosition();

//            homeFragmentPresenter.updateUserRelationships(
//                    this, data.get(position).getObjectId(), v.getTag().toString());
            v.setClickable(false);
        }



    }
}
