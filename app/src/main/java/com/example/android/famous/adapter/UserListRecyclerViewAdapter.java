package com.example.android.famous.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.famous.R;
import com.example.android.famous.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 9/15/15.
 */
public class UserListRecyclerViewAdapter
        extends RecyclerView.Adapter<UserListRecyclerViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<User> data = new ArrayList<>();
    private Context context;

    public UserListItemClickedCallback userListItemClickedListener;

    public UserListRecyclerViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
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

    public void swapData(List<User> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView userListProfilePicture;
        TextView userListUserName;
        TextView userListFullName;
        TextView userListFollow;
        ImageView userListClear;

        public MyViewHolder(View itemView) {
            super(itemView);

            userListProfilePicture = (ImageView) itemView.findViewById(R.id.user_list_avatar);
            userListUserName = (TextView) itemView.findViewById(R.id.userListUserName);
            userListFullName = (TextView) itemView.findViewById(R.id.userListFullName);
            userListFollow = (Button) itemView.findViewById(R.id.follow_user_button);
            userListClear = (ImageView) itemView.findViewById(R.id.userListClear);

            userListProfilePicture.setOnClickListener(this);
            userListUserName.setOnClickListener(this);
            userListFullName.setOnClickListener(this);
            userListFollow.setOnClickListener(this);
            userListClear.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(context, "CLICKED", Toast.LENGTH_SHORT).show();

            int position = -1;
            if (getAdapterPosition() == getLayoutPosition()) {
                position = getAdapterPosition();

                switch(v.getId()) {
                    case R.id.follow_user_button:
                        userListItemClickedListener.UserListItemClickedCallback(
                                data.get(position).getObjectId(), v);
                        break;
                }

            }
//            homeFragmentPresenter.updateUserRelationships(
//                    data.get(position).getObjectId(), v);
            v.setClickable(false);
        }
    }

    public interface UserListItemClickedCallback {
        void UserListItemClickedCallback(String objectId, View view);
    }
}
