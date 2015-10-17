package com.hash.looop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 11/10/15.
 */
public class FollowResponse {

    @SerializedName("status")
    @Expose
    private int mStatus;
    @SerializedName("message")
    @Expose
    private String mMessage;
    @SerializedName("followed_id")
    @Expose
    private String mFollowedId;

    public String getFollowedId() {
        return mFollowedId;
    }

    public void setFollowedId(String followedId) {
        mFollowedId = followedId;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }
}
