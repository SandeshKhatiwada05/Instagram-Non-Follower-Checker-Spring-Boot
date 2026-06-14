package com.mainframe.instagram.model;

import java.util.List;

public class AnalysisResult {
    private List<InstagramUser> nonFollowers;
    private int followersCount;
    private int followingCount;
    private int nonFollowersCount;
    private String error;

    public AnalysisResult() {}

    public List<InstagramUser> getNonFollowers() { return nonFollowers; }
    public void setNonFollowers(List<InstagramUser> nonFollowers) {
        this.nonFollowers = nonFollowers;
    }

    public int getFollowersCount() { return followersCount; }
    public void setFollowersCount(int followersCount) { this.followersCount = followersCount; }

    public int getFollowingCount() { return followingCount; }
    public void setFollowingCount(int followingCount) { this.followingCount = followingCount; }

    public int getNonFollowersCount() { return nonFollowersCount; }
    public void setNonFollowersCount(int nonFollowersCount) { this.nonFollowersCount = nonFollowersCount; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
