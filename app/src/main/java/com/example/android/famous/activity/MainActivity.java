package com.example.android.famous.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.famous.adapter.TabsPagerAdapter;
import com.example.android.famous.adapter.UserListRecyclerViewAdapter;
import com.example.android.famous.dialog.SuggestionsDialogFragment;
import com.example.android.famous.fragment.ActivityFragment;
import com.example.android.famous.fragment.HomeFragment;
import com.example.android.famous.fragment.ProfileFragment;
import com.example.android.famous.fragment.SearchFragment;
import com.example.android.famous.R;
import com.example.android.famous.presenter.HomeFragmentPresenter;
import com.example.android.famous.util.Utils;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity
        implements SuggestionsDialogFragment.OnUserListRecyclerViewAdapterAvailableCallback,
        SuggestionsDialogFragment.UserListItemClickedCallback {

    public final String TAG = this.getClass().getSimpleName();

    public static final String VIEW_PAGER_POSITION = "viewPagerPosition";

    public static final int GALLERY_FRAGMENT_ID = 0;
    private static final int CAMERA_FRAGMENT_ID = 1;
    private static final int VIDEO_FRAGMENT_ID = 2;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerMain);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsMain);

        if (viewPager != null) {
            setUpViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }

        // temp
        deleteDatabase("famousDatabase");

        intent = new Intent(getApplicationContext(), PhotoActivity.class);

        findViewById(R.id.select_picture_gallery).setOnClickListener(pictureOnClickListener);
        findViewById(R.id.select_picture_camera).setOnClickListener(cameraOnClickListener);
        findViewById(R.id.select_video).setOnClickListener(videoOnClickListener);

    }

    private View.OnClickListener pictureOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra(VIEW_PAGER_POSITION, GALLERY_FRAGMENT_ID);
            startActivity(intent);
        }
    };

    private View.OnClickListener cameraOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra(VIEW_PAGER_POSITION, CAMERA_FRAGMENT_ID);
            startActivity(intent);
        }
    };

    private View.OnClickListener videoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra(VIEW_PAGER_POSITION, VIDEO_FRAGMENT_ID);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Logout:
                ParseUser.logOut();
                finish();
                break;
            case R.id.suggestions_button:
                SuggestionsDialogFragment suggestionsDialogFragment = SuggestionsDialogFragment.newInstance();
                suggestionsDialogFragment.onUserListRecyclerViewAdapterAvailableListener = this;
                suggestionsDialogFragment.userListItemClickedListener = this;
                suggestionsDialogFragment.show(getSupportFragmentManager(), null);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpViewPager(ViewPager viewPager) {
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), this);

        adapter.addFragment(new HomeFragment(), R.drawable.ic_action_home_dark);
        adapter.addFragment(new SearchFragment(), R.drawable.ic_action_search_dark);
        adapter.addFragment(new ActivityFragment(), R.drawable.ic_action_activity_dark);
        adapter.addFragment(new ProfileFragment(), R.drawable.ic_action_person_dark);

        viewPager.setAdapter(adapter);

    }

    @Override
    public void onUserListRecyclerViewAdapterAvailableCallback(
            UserListRecyclerViewAdapter userListRecyclerViewAdapter) {
        HomeFragmentPresenter.getInstance().fetchSuggestedUserList(userListRecyclerViewAdapter);

    }

    @Override
    public void userListItemClickedCallback(String objectId, View view) {
        HomeFragmentPresenter.getInstance().updateUserRelationships(objectId, view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("GLOBAL", TAG + "_DESTROYED");
    }
}
