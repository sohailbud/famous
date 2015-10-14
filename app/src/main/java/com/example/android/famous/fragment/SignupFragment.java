package com.example.android.famous.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.famous.activity.MainActivity;
import com.example.android.famous.model.User;
import com.example.android.famous.presenter.AccessPresenter;
import com.example.android.famous.R;
import com.example.android.famous.callback.SignUp;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment  implements SignUp {

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void newUserCreateDone(boolean isSuccess, String description) {
        if (isSuccess) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final AccessPresenter accessPresenter = AccessPresenter.getInstance();
        accessPresenter.signUp = this;

        getActivity().findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) getActivity().findViewById(R.id.input_name)).getText().toString();
                String email = ((EditText) getActivity().findViewById(R.id.input_email)).getText().toString();
                String userName = ((EditText) getActivity().findViewById(R.id.input_username)).getText().toString();
                String password = ((EditText) getActivity().findViewById(R.id.input_password)).getText().toString();

                accessPresenter.createNewParseUser(name, email, userName, password);
            }
        });
    }
}
