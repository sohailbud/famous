package com.example.android.famous.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.famous.R;
import com.example.android.famous.fragment.AccessActivityFragment;
import com.example.android.famous.presenter.AccessPresenter;

public class AccessActivity extends AppCompatActivity {

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
        }
    }

}
