package com.hash.loop.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 4/10/15.
 */
public class PostLooopRequest {

    @SerializedName("user_id")
    private String mUserId;
    @SerializedName("latitude")
    private Double mLat;
    @SerializedName("longitude")
    private Double mLong;
    @SerializedName("status")
    private String mStatus;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double lat) {
        mLat = lat;
    }

    public Double getLong() {
        return mLong;
    }

    public void setLong(Double aLong) {
        mLong = aLong;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}
