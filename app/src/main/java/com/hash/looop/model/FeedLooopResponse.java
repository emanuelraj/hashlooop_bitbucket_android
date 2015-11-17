package com.hash.looop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FeedLooopResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("looops")
    @Expose
    private List<Looop> looops = new ArrayList<Looop>();
    @SerializedName("max_id")
    private int mMaxId;
    @SerializedName("min_id")
    private int mMinId;
    @SerializedName("response_for")
    private int mResponseType;

    public int getResponseType() {
        return mResponseType;
    }

    public void setResponseType(int responseType) {
        mResponseType = responseType;
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

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The looops
     */
    public List<Looop> getLooops() {
        return looops;
    }

    /**
     * @param looops The looops
     */
    public void setLooops(List<Looop> looops) {
        this.looops = looops;
    }

}
