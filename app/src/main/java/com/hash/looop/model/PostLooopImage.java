package com.hash.looop.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 25/10/15.
 */
public class PostLooopImage {
    @SerializedName("user_id")
    private String mUserId;
    @SerializedName("latitude")
    private Double mLat;
    @SerializedName("longitude")
    private Double mLong;
    @SerializedName("caption")
    private String mCaption;
    @SerializedName("image")
    private byte[] mImageArray;

    public byte[] getImageArray() {
        return mImageArray;
    }

    public void setImageArray(byte[] imageArray) {
        mImageArray = imageArray;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
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
