package com.hash.looop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mathan on 1/11/15.
 */
public class NotificationReponse {
    @SerializedName("status")
    @Expose
    private Integer mStatus;
    @SerializedName("message")
    @Expose
    private String mMessage;
    @SerializedName("looop_user_id")
    @Expose
    private String mUserId;

}
