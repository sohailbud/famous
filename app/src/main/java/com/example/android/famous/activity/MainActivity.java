package com.example.android.famous.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.famous.adapter.TabsPagerAdapter;
import com.example.android.famous.fragment.ActivityFragment;
import com.example.android.famous.fragment.HomeFragment;
import com.example.android.famous.fragment.ProfileFragment;
import com.example.android.famous.fragment.SearchFragment;
import com.example.android.famous.R;
import com.example.android.famous.model.Location;
import com.example.android.famous.presenter.CameraPresenter;
import com.parse.ParseUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

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


        camera();
    }

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

    public void camera() {
        findViewById(R.id.selectPictureCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create Intent to take a picture and return control to the calling application
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // start the image capture Intent
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    /**
     * Create a file Uri for saving an image or video
     * @param type
     * @return
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     * @param type
     * @return
     */
    public File getOutputMediaFile(int type) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), this.getString(R.string.app_name));
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()) {
                if (! mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                    Log.d("PATH", mediaStorageDir.toString());
                    return null;
                }
            }

            // Create a media file name
            String timeCreated = new SimpleDateFormat(this.getString(R.string.createdTimeFormat)).
                    format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_" + timeCreated + ".jpg");

            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_" + timeCreated + ".mp4");

            } else return null;

            return mediaFile;

        } else return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        CameraPresenter cameraPresenter = CameraPresenter.getInstance();

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                // Create new Feed object and save it to parse
                cameraPresenter.saveParseObject(data.getData(), this, new Location(22.00, 21.43));

            } else if(resultCode == RESULT_CANCELED) {
                // User canceled image capture

            } else {
                Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
