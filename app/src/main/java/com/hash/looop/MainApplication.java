package com.hash.looop;

import android.app.Application;
import android.content.Intent;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mathan on 4/10/15.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        startService(new Intent(getApplicationContext(), SocketService.class));
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
