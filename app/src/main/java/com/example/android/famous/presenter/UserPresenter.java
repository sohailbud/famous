package com.example.android.famous.presenter;

import com.example.android.famous.R;
import com.example.android.famous.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 10/6/15.
 */
public class UserPresenter {

    private static UserPresenter userPresenter = null;

    public UserPresenter() {
    }

    public static UserPresenter getInstance() {
        if (userPresenter == null) userPresenter = new UserPresenter();
        return userPresenter;
    }





}
