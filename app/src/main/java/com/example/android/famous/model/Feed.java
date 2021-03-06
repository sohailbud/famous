package com.example.android.famous.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 10/7/15.
 */
public class Feed {

    private String objectId;
    private String createdAt;
    private Location location;
    private User user;
    private String mediaURI;
    private String tags;

    private List<Comment> commentList = new ArrayList<>();
    private List<User> likesList = new ArrayList<>();

    public Feed(Location location, User user, String mediaURI) {
        this.location = location;
        this.user = user;
        this.mediaURI = mediaURI;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<User> getLikesList() {
        return likesList;
    }

    public void setLikesList(List<User> likesList) {
        this.likesList = likesList;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getMediaURI() {
        return mediaURI;
    }

    public void setMediaURI(String mediaURI) {
        this.mediaURI = mediaURI;
    }
}
