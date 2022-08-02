package com.security.applock.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.security.applock.service.AntiTheftService;
import com.security.applock.service.BackgroundManager;

public class WatchmenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (BackgroundManager.getInstance(context).canStartService()) {
            BackgroundManager.getInstance(context).startService(AntiTheftService.class);
        }
    }
}
