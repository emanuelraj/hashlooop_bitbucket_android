package com.hash.looop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Looop {

    @SerializedName("looop_id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @Expose
    private String name;
    @Expose
    private int relationship;
    @SerializedName("image_url")
    @Expose
    private String mImageUrl;
    @SerializedName("status_type")
    @Expose
    private Integer mStatusType;
    @SerializedName("total_likes")
    @Expose
    private Integer mTotalLikes;
    @SerializedName("posted_time")
    @Expose
    private String mPostedTime;
    @SerializedName("like_status")
    @Expose
    private Integer isLiked;

    public Integer getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Integer isLiked) {
        this.isLiked = isLiked;
    }

    public String getPostedTime() {
        return mPostedTime;
    }

    public void setPostedTime(String postedTime) {
        mPostedTime = postedTime;
    }

    public Integer getTotalLikes() {
        return mTotalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        mTotalLikes = totalLikes;
    }

    public Integer getStatusType() {
        return mStatusType;
    }

    public void setStatusType(Integer statusType) {
        mStatusType = statusType;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public int getRelationship() {
        return relationship;
    }

    public void setRelationship(int relationship) {
        this.relationship = relationship;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The distance
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * @param distance The distance
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

}
