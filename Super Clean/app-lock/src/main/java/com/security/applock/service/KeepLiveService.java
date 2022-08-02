package com.security.applock.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

public class KeepLiveService extends IntentService {

    public KeepLiveService() {
        super("KeepLiveService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (BackgroundManager.getInstance(this).canStartService()) {
            BackgroundManager.getInstance(this).startService(AntiTheftService.class);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            startService(new Intent(KeepLiveService.this, KeepLiveService.class));
        } catch (Exception e) {

        }
    }
}
