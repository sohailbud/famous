package com.example.android.famous.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 10/7/15.
 */
public class Feed {

    int id;
    String createdTime;
    Location location;
    User user;
    Bitmap media;

    List<Comment> commentList = new ArrayList<>();
    List<User> likesList = new ArrayList<>();
    List<String> tags = new ArrayList<>();
    List<User> usersInPhoto = new ArrayList<>();

    public Feed(Location location, User user, Bitmap media) {
        this.location = location;
        this.user = user;
        this.media = media;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
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

    public Bitmap getMedia() {
        return media;
    }

    public void setMedia(Bitmap media) {
        this.media = media;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<User> getUsersInPhoto() {
        return usersInPhoto;
    }

    public void setUsersInPhoto(List<User> usersInPhoto) {
        this.usersInPhoto = usersInPhoto;
    }
}
