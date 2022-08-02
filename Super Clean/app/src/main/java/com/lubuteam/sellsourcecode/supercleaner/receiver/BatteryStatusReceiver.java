package com.lubuteam.sellsourcecode.supercleaner.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.lubuteam.sellsourcecode.supercleaner.model.BatteryInfo;
import com.lubuteam.sellsourcecode.supercleaner.service.NotificationUtil;
import com.lubuteam.sellsourcecode.supercleaner.service.ServiceManager;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;

public class BatteryStatusReceiver extends BroadcastReceiver {

    private BatteryInfo mBatteryInfo = new BatteryInfo();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
            mBatteryInfo.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            mBatteryInfo.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            mBatteryInfo.temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            mBatteryInfo.voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            mBatteryInfo.technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            //charging
            mBatteryInfo.status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            //Day pin
            if (mBatteryInfo.status == BatteryManager.BATTERY_STATUS_FULL && PreferenceUtils.isPowerConnected() && PreferenceUtils.isNotifiBatteryFull())
                NotificationUtil.getInstance().fullPower(context);
        } else if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
            PreferenceUtils.setPowerConnected(true);
            if (PreferenceUtils.isOnSmartCharger() && ServiceManager.getInstance() != null)
                ServiceManager.getInstance().startSmartRecharger();
        } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            PreferenceUtils.setPowerConnected(false);
        }
    }

    public final void OnCreate(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(this, intentFilter);
    }

    public final void OnDestroy(Context context) {
        if (context != null) {
            context.unregisterReceiver(this);
        }
    }
}
