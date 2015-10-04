package com.hash.loop;

import android.app.Application;
import android.content.Intent;

import timber.log.Timber;

/**
 * Created by mathan on 4/10/15.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        startService(new Intent(getApplicationContext(),SocketService.class));
    }
}
