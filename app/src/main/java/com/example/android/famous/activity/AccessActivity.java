package com.example.android.famous.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.famous.R;
import com.example.android.famous.fragment.AccessActivityFragment;
import com.example.android.famous.presenter.AccessPresenter;

public class AccessActivity extends AppCompatActivity {

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        AccessPresenter loginPresenter = AccessPresenter.getInstance();

        if (loginPresenter.isUserAnonymous()) {
            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.accessFragmentContainer,
                    new AccessActivityFragment()).commit();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("GLOBAL", TAG + "_DESTROYED");
    }
}
