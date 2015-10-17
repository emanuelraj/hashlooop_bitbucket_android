package com.hash.looop.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mathan on 26/9/15.
 */
public class MySharedPreference {
    private Context mContext;
    private SharedPreferences mPrefs;

    public MySharedPreference(Context context) {
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void setUserId(String userId) {
        setTextType(Constants.USER_ID, userId);
    }

    public void setUserName(String userName) {
        setTextType(Constants.USER_NAME, userName);
    }

    private void setTextType(String type, String value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(type, value);
        editor.apply();
    }

    public String getUserId() {
        return mPrefs.getString(Constants.USER_ID,"");
    }
}
