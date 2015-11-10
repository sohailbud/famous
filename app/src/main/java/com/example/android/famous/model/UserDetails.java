package com.example.android.famous.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sohail on 10/6/15.
 */
public class UserDetails {

    private User user;
    private String website;
    private String bio;
    private String gender;
    private String email;
    private String phoneNumber;

    private List<User> follows = new ArrayList<>();
    private List<User> followedBy = new ArrayList<>();
    private List<User> requestedBy = new ArrayList<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<User> getFollows() {
        return follows;
    }

    public void setFollows(List<User> follows) {
        this.follows = follows;
    }

    public List<User> getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(List<User> followedBy) {
        this.followedBy = followedBy;
    }

    public List<User> getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(List<User> requestedBy) {
        this.requestedBy = requestedBy;
    }
}
