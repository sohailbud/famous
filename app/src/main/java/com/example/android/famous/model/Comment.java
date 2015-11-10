package com.example.android.famous.model;

import android.text.format.DateFormat;

/**
 * Created by Sohail on 9/17/15.
 */
public class Comment {

    private String objectId;
    private String createdTime;
    private String text;
    private User user;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
