package com.hash.looop.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 4/10/15.
 */
public class FetchLoopsRequest {

    @SerializedName("user_id")
    private String mUserId;
    @SerializedName("latitude")
    private Double mLat;
    @SerializedName("longitude")
    private Double mLong;
    @SerializedName("max_id")
    private int mMaxId;
    @SerializedName("min_id")
    private int mMinId;
    @SerializedName("data_fetch_status")
    private int mFetchRequest;

    public int getFetchRequest() {
        return mFetchRequest;
    }

    public void setFetchRequest(int fetchRequest) {
        mFetchRequest = fetchRequest;
    }

    public int getMaxId() {
        return mMaxId;
    }

    public void setMaxId(int maxId) {
        mMaxId = maxId;
    }

    public int getMinId() {
        return mMinId;
    }

    public void setMinId(int minId) {
        mMinId = minId;
    }

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
}
