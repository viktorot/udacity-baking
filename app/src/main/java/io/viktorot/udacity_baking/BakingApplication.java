package io.viktorot.udacity_baking;

import android.app.Application;

import timber.log.Timber;

public class BakingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
