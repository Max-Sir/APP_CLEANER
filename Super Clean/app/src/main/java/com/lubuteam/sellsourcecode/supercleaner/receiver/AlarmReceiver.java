package com.lubuteam.sellsourcecode.supercleaner.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.core.content.ContextCompat;

import com.lubuteam.sellsourcecode.supercleaner.data.TaskBattery;
import com.lubuteam.sellsourcecode.supercleaner.data.TotalRamTask;
import com.lubuteam.sellsourcecode.supercleaner.service.NotificationUtil;
import com.lubuteam.sellsourcecode.supercleaner.service.ServiceManager;
import com.lubuteam.sellsourcecode.supercleaner.utils.AlarmUtils;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;
import com.lubuteam.sellsourcecode.supercleaner.utils.SystemUtil;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AlarmUtils.ACTION_AUTOSTART_ALARM)) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NAG");
            wl.acquire();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (!isMyServiceRunning(ServiceManager.class, context) && extras.getBoolean(AlarmUtils.ACTION_REPEAT_SERVICE, Boolean.FALSE)) {
                    try {
                        if (ServiceManager.getInstance() != null)
                            ServiceManager.getInstance().onDestroy();
                    } catch (Exception e) {

                    }
                    Intent mIntent = new Intent(context, ServiceManager.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ContextCompat.startForegroundService(context, mIntent);
                    } else {
                        context.startService(new Intent(context, ServiceManager.class));
                    }
                }
                if (extras.getBoolean(AlarmUtils.ALARM_PHONE_BOOOST)) {
                    PreferenceUtils.setTimeAlarmPhoneBoost(AlarmUtils.TIME_PHONE_BOOST);
                    AlarmUtils.setAlarm(context, AlarmUtils.ALARM_PHONE_BOOOST, AlarmUtils.TIME_PHONE_BOOST);
                    new TotalRamTask((useRam, totalRam) -> {
                        float progress = (float) useRam / (float) totalRam;
                        if (progress * 100 > AlarmUtils.RAM_USE && SystemUtil.checkDNDDoing()) {
                            NotificationUtil.getInstance().showNotificationAlarm(context, NotificationUtil.ID_NOTIFICATTION_PHONE_BOOST);
                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else if (extras.getBoolean(AlarmUtils.ALARM_PHONE_CPU_COOLER)) {
                    PreferenceUtils.setTimeAlarmPhoneBoost(AlarmUtils.TIME_CPU_COOLER);
                    AlarmUtils.setAlarm(context, AlarmUtils.ALARM_PHONE_CPU_COOLER, AlarmUtils.TIME_CPU_COOLER);
                    new TaskBattery(context, BatteryManager.EXTRA_TEMPERATURE, pin -> {
                        if (pin > AlarmUtils.BATTERY_HOT && SystemUtil.checkDNDDoing())
                            NotificationUtil.getInstance().showNotificationAlarm(context, NotificationUtil.ID_NOTIFICATTION_CPU_COOLER);
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else if (extras.getBoolean(AlarmUtils.ALARM_PHONE_BATTERY_SAVE)) {
                    PreferenceUtils.setTimeAlarmPhoneBoost(AlarmUtils.TIME_BATTERY_SAVE);
                    AlarmUtils.setAlarm(context, AlarmUtils.ALARM_PHONE_BATTERY_SAVE, AlarmUtils.TIME_BATTERY_SAVE);
                    new TaskBattery(context, BatteryManager.EXTRA_LEVEL, pin -> {
                        if (pin < AlarmUtils.BATTERY_LOW && SystemUtil.checkDNDDoing())
                            NotificationUtil.getInstance().showNotificationAlarm(context, NotificationUtil.ID_NOTIFICATTION_BATTERY_SAVE);
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else if (extras.getBoolean(AlarmUtils.ALARM_PHONE_JUNK_FILE)) {
                    AlarmUtils.setAlarm(context, AlarmUtils.ALARM_PHONE_JUNK_FILE, Toolbox.getTimeAlarmJunkFile(false));
                    if (SystemUtil.checkDNDDoing())
                        NotificationUtil.getInstance().showNotificationAlarm(context, NotificationUtil.ID_NOTIFICATTION_JUNK_FILE);
                }
            }

        }
    }

    public final void OnCreate(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AlarmUtils.ACTION_AUTOSTART_ALARM);
        context.registerReceiver(this, intentFilter);
    }

    public final void OnDestroy(Context context) {
        if (context != null) {
            context.unregisterReceiver(this);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass, Context mContext) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
