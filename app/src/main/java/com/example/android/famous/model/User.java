package com.example.android.famous.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 9/17/15.
 */
public class User {

    private String objectId;
    private String username;
    private String fullName;
    private int profilePicture;

    public User(String objectId, String username, String fullName) {
        this.objectId = objectId;
        this.username = username;
        this.fullName = fullName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }
}
