package com.hash.looop.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 10/10/15.
 */
public class TrendingLooopsRequest {

    @SerializedName("user_id")
    public String mUserId;
    @SerializedName("latitude")
    public String mLatitude;
    @SerializedName("longitude")
    public String mLongitude;


    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }
}