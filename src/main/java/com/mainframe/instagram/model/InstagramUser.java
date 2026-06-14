package com.mainframe.instagram.model;

public class InstagramUser {
    private String username;
    private String fullName;
    private String profileUrl;

    public InstagramUser() {}

    public InstagramUser(String username, String fullName, String profileUrl) {
        this.username = username;
        this.fullName = fullName;
        this.profileUrl = profileUrl;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getProfileUrl() { return profileUrl; }
    public void setProfileUrl(String profileUrl) { this.profileUrl = profileUrl; }
}
