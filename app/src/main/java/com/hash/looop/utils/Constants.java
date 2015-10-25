package com.hash.looop.utils;

/**
 * Created by mathan on 26/8/15.
 */
public class Constants {
    public static final String SOCKET_URL = "http://ec2-54-254-235-212.ap-southeast-1.compute.amazonaws.com:3000/";

    //Request events
    public static final String REGISTRATION_REQUEST = "new_registration";
    public static final String LOGIN_REQUEST = "login";
    public static final String NEW_LOOOP = "new_looop";
    public static final String FETCH_LOOOPS_REQUEST = "fetch_feed_looops";
    public static final String RECONNECT = "socket_reconnect";
    public static final String LIKE_REQUEST = "like_looops";
    public static final String TRENDING_LOOOPS_REQUEST = "fetch_trending_looops";
    public static final String NEW_FOLLOW_REQUEST = "new_follow";
    public static final String NEW_IMAGE_LOOOP = "new_image_looop";

    //Response events
    public static final String REGISTRATION_RESPONSE = "registration_response";
    public static final String LOGIN_RESPONSE = "login_response";
    public static final String LOOOP_POST_RESPONSE = "looop_success";
    public static final String FETCH_LOOOPS_RESPONSE = "looop_in_that_location";
    public static final String RECONNECT_RESPONSE = "socket_reconnect_status";
    public static final String LOOOP_SUCCESS = "looop_success";
    public static final String LIKE_RESPONSE = "like_response";
    public static final String TRENDING_LOOOPS_RESPONSE = "trending_looop_in_that_location";
    public static final String FOLLOW_RESPONSE = "follow_response";

    //Preference keys
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";

    public static final int OTHER = 1;
    public static final int ME = 2;
    public static final String UNIQUE_FILE_NAME = "looop_image";
}
