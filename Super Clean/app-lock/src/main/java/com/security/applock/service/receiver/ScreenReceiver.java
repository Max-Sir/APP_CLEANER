package com.security.applock.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.security.applock.service.AntiTheftService;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;

public class ScreenReceiver extends BroadcastReceiver {
    private ScreenCallback screenCallback;

    public void setScreenCallback(ScreenCallback screenCallback) {
        this.screenCallback = screenCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_ON:
                screenCallback.onScrenOn();
                break;
            case Intent.ACTION_SCREEN_OFF:
                screenCallback.onScrenOff();
                break;
        }
    }

    public final void onCreate(Context context) {
        IntentFilter screenFilter = new IntentFilter();
        screenFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(this, screenFilter);
    }

    public final void onDestroy(Context context) {
        try{
            context.unregisterReceiver(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface ScreenCallback {
        void onScrenOn();

        void onScrenOff();
    }
}

