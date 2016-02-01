package com.example.android.famous.util.parse;

import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;
/**
 * Created by Sohail on 9/29/15.
 */
public class ParseApplication extends Application {

    final String  APPLICATION_ID = "bN93JjxCV1aRI0MimghKZ6IT5ktolH1nxAOMJMno";
    final String CLIENT_KEY = "IeXVXpBpStUv075Z2ni2lGNx7rf6hjh2nzs4NuIu";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        // Add your initialization code here
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

        ParseUser.enableRevocableSessionInBackground();

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
