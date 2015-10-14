package com.example.android.famous.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.famous.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccessActivityFragment extends Fragment {

    public AccessActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_access, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonClickListener();
    }

    public void buttonClickListener() {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        getActivity().findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.accessFragmentContainer,
                        new LoginFragment()).addToBackStack(null).commit();
            }
        });

        getActivity().findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.accessFragmentContainer,
                        new SignupFragment()).addToBackStack(null).commit();
            }
        });

    }

}
