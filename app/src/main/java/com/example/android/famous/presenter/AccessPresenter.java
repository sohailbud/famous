package com.example.android.famous.presenter;

import android.util.Log;

import com.example.android.famous.model.User;
import com.example.android.famous.callback.Login;
import com.example.android.famous.callback.SignUp;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

/**
 * Created by Sohail on 9/29/15.
 */
public class AccessPresenter {

    public SignUp signUp;
    public Login login;
    private static AccessPresenter accessPresenter = null;
    private static User user;

    public AccessPresenter() {
        super();
    }

    public static AccessPresenter getInstance() {
        if (accessPresenter == null) accessPresenter = new AccessPresenter();
        return accessPresenter;
    }

    public  void createNewParseUser(String fullName, String email, String userName, String password) {
        final ParseUser newUser = new ParseUser();
        newUser.setUsername(userName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.put("fullName", fullName);

        final ParseObject newUserDetails = new ParseObject("UserDetails");
        final ParseACL ACL = new ParseACL();
        ACL.setPublicWriteAccess(true);
        ACL.setPublicReadAccess(true);

        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    signUp.newUserCreateDone(true, null);
                    newUserDetails.put("User", newUser);
                    newUserDetails.setACL(ACL);
                    newUserDetails.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) e.printStackTrace();
                        }
                    });

                } else {
                    e.printStackTrace();
                    signUp.newUserCreateDone(false, e.getMessage());
                }
            }
        });
    }

    public void verifyParseUser(String userName, String password) {
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    login.parseUserVerified(true, null);
                } else {
                    login.parseUserVerified(false, e.getMessage());
                }
            }
        });
    }

    public boolean isUserAnonymous() {
        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginSignupActivity.class
            return true;
        } else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static User getCurrentUser() {
        if (user == null) user = convertParseUserToUser(ParseUser.getCurrentUser());
        return user;
    }

    public static User convertParseUserToUser(ParseUser parseUser) {
        user = new User(parseUser.getObjectId(), parseUser.getUsername(), (String) parseUser.get("fullName"));
        return user;
    }
}
