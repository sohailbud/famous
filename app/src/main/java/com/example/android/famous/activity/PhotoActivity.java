package com.example.android.famous.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.android.famous.fragment.camera.CameraFragment;
import com.example.android.famous.fragment.camera.GalleryFragment;
import com.example.android.famous.fragment.camera.VideoFragment;
import com.example.android.famous.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {

    public final String TAG = this.getClass().getSimpleName();

    private int viewPagerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Log.i("CACHE DIR", getCacheDir().toString());

        viewPagerPosition = getIntent().getIntExtra(
                MainActivity.VIEW_PAGER_POSITION, MainActivity.GALLERY_FRAGMENT_ID);

        // tab layout and viewpager views
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // setup viewpager
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        // sets window to full screen by hiding the status bar
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    /**
     * set up view pager by adding fragments to the page adapter class
     */
    private void setupViewPager(ViewPager viewPager) {
        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(new GalleryFragment(), "GALLERY");
        pagerAdapter.addFragment(new CameraFragment(), "CAMERA");
        pagerAdapter.addFragment(new VideoFragment(), "VIDEO");
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(viewPagerPosition);
    }

    /**
     * A PageAdapter class to set up view pager
     */
    public class TabsPagerAdapter extends FragmentStatePagerAdapter {

        // store the fragment class and title of the pages
        private final List<Fragment> FRAGMENTS_LIST = new ArrayList<>();
        private final List<String> FRAGMENTS_TITLES = new ArrayList<>();

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FRAGMENTS_LIST.get(position);
        }

        @Override
        public int getCount() {
            return FRAGMENTS_LIST.size() != 0 ? FRAGMENTS_LIST.size() : 0;
        }

        public void addFragment(Fragment fragment, String title) {
            FRAGMENTS_LIST.add(fragment);
            FRAGMENTS_TITLES.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return FRAGMENTS_TITLES.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("GLOBAL", TAG + "_DESTROYED");
    }
}
