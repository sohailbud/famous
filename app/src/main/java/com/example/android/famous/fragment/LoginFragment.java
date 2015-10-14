package com.example.android.famous.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.famous.R;
import com.example.android.famous.activity.MainActivity;
import com.example.android.famous.presenter.AccessPresenter;
import com.example.android.famous.callback.Login;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements Login {

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final AccessPresenter accessPresenter = AccessPresenter.getInstance();
        accessPresenter.login = this;

        getActivity().findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = ((EditText) getActivity().findViewById(R.id.input_email)).getText().toString();
                String password = ((EditText) getActivity().findViewById(R.id.input_password)).getText().toString();

                accessPresenter.verifyParseUser(userName, password);
            }
        });
    }

    @Override
    public void parseUserVerified(Boolean isVerified, String description) {
        if (isVerified) {
            startActivity(
                    new Intent(getActivity(), MainActivity.class));
        } else {
            Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
        }
    }
}
