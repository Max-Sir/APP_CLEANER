package com.lubuteam.sellsourcecode.supercleaner.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.lubuteam.sellsourcecode.supercleaner.service.ServiceManager;

public class AutoRebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)
                ||action.equals("android.intent.action.QUICKBOOT_POWERON")
                ||action.equals("com.htc.intent.action.QUICKBOOT_POWERON")
                ||action.equalsIgnoreCase("android.intent.action.SCREEN_ON")
                || action.equalsIgnoreCase("android.intent.action.USER_PRESENT")
                || action.equalsIgnoreCase("android.intent.action.ACTION_POWER_CONNECTED")
                || action.equalsIgnoreCase("android.net.wifi.WIFI_STATE_CHANGED")
                || action.equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE")
                || action.equalsIgnoreCase("android.net.wifi.STATE_CHANGE")
        ) {
            Intent intent2 = new Intent(context, ServiceManager.class);
            ContextCompat.startForegroundService(context, intent2);

        }
    }
}
