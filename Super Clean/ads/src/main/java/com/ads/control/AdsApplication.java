package com.ads.control;

import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;

public class AdsApplication extends MultiDexApplication {

    private static AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(
                this,
                initializationStatus -> {
                });
        appOpenManager = new AppOpenManager(this);
    }
}
