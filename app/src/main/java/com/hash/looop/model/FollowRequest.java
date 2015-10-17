package com.hash.looop.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 11/10/15.
 */
public class FollowRequest {

    @SerializedName("following_id")
    private String mFollowingId;
    @SerializedName("user_id")
    private String mUserId;

    public String getFollowingId() {
        return mFollowingId;
    }

    public void setFollowingId(String followingId) {
        mFollowingId = followingId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }
}
