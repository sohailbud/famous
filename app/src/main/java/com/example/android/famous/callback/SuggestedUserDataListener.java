package com.example.android.famous.callback;

import com.example.android.famous.model.User;

import java.util.List;

/**
 * Created by Sohail on 10/12/15.
 */
public interface SuggestedUserDataListener {

    void suggestedUserDataOnProcess(List<User> data);
}
