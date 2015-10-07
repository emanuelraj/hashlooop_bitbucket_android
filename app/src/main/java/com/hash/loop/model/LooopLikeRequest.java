package com.hash.loop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 8/10/15.
 */
public class LooopLikeRequest {

    @SerializedName("user_id")
    @Expose
    private String mUserId;
    @SerializedName("looop_id")
    @Expose
    private String mLooopId;

    public String getLooopId() {
        return mLooopId;
    }

    public void setLooopId(String looopId) {
        mLooopId = looopId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

}
