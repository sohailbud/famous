package com.example.android.famous.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.android.famous.R;
import com.example.android.famous.adapter.TabsPagerAdapter;
import com.example.android.famous.fragment.common.FeedFragment;
import com.example.android.famous.fragment.common.PhotosGridFragment;
import com.example.android.famous.presenter.UserPresenter;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarUser);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerUser);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsUser);

        if (viewPager != null) {
            setUpViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setUpViewPager(ViewPager viewPager) {

        UserPresenter userPresenter = UserPresenter.getInstance();

        // Tab1
        PhotosGridFragment photosGridFragmentUserPosts = new PhotosGridFragment();
        photosGridFragmentUserPosts.setGridData(null);

        // Tab2
        FeedFragment postFeedFragment = new FeedFragment();
        // Tab3 (Map)

        // Tab4
        PhotosGridFragment photosGridFragmentUserPhotos = new PhotosGridFragment();
        photosGridFragmentUserPhotos.setGridData(null);

        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), this);

        adapter.addFragment(photosGridFragmentUserPosts, R.drawable.ic_action_grid);
        adapter.addFragment(postFeedFragment, R.drawable.ic_action_linear);
        adapter.addFragment(new FeedFragment(), R.drawable.ic_action_location_on);
        adapter.addFragment(photosGridFragmentUserPhotos, R.drawable.ic_action_person_pin);

        viewPager.setAdapter(adapter);
    }
}
