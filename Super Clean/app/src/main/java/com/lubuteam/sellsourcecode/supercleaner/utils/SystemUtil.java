package com.lubuteam.sellsourcecode.supercleaner.utils;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;


import java.util.Calendar;
import java.util.List;

public class SystemUtil {

    public static boolean isHasGetOtherAppInfoPermission(Context context) {
        long ts = System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= 21) {
            List<UsageStats> queryUsageStats = ((UsageStatsManager) context.getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE)).queryUsageStats(4, 0, ts);
            if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkCanWriteSettings(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context)) {
            return false;
        }
        return true;
    }

    public static boolean isNotificationListenerEnabled(Context context) {
        try {
            String packageName = context.getApplicationContext().getPackageName();
            String str1 = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
            if (TextUtils.isEmpty(str1)) {
                return false;
            }
            for (String string : str1.split(":")) {
                ComponentName localComponentName = ComponentName.unflattenFromString(string);
                if (localComponentName != null && TextUtils.equals(packageName, localComponentName.getPackageName())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static final boolean isUsageAccessAllowed(Context mContext) {
        boolean granted = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                AppOpsManager appOps = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
                int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), mContext.getPackageName());
                if (mode == AppOpsManager.MODE_DEFAULT) {
                    String permissionUsage = "android.permission.PACKAGE_USAGE_STATS";
                    granted = (mContext.checkCallingOrSelfPermission(permissionUsage) == PackageManager.PERMISSION_GRANTED);
                } else {
                    granted = (mode == AppOpsManager.MODE_ALLOWED);
                }
            } catch (Throwable e) {
            }
        } else {
            return true;
        }
        return granted;
    }

    public static String getLauncherTopApp(@NonNull Context context, ActivityManager activityManager) {
        //isLockTypeAccessibility = SpUtil.getInstance().getBoolean(AppConstants.LOCK_TYPE, false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } else {
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageStatsManager sUsageStatsManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            }
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                }
            }
            if (!android.text.TextUtils.isEmpty(result)) {
                return result;
            }
        }
        return "";
    }

    public static void intSound(Context mContext) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(mContext, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void openMarket(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + context.getPackageName() ));
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            try {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void shareApp(Context mContext, String content, String title) {
        try {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = content;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share App");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            mContext.startActivity(Intent.createChooser(sharingIntent, title));
        } catch (Exception e) {

        }
    }

    public static boolean checkDNDDoing() {
        Calendar instance = Calendar.getInstance();
        boolean b = true;
        int now = (instance.get(Calendar.HOUR_OF_DAY) * 100) + instance.get(Calendar.MINUTE);
        int dNDStartTime = PreferenceUtils.getDNDStart();
        int dNDStopTime = PreferenceUtils.getDNDEnd();
        if (dNDStopTime < dNDStartTime) {
            dNDStopTime += 2400;
        }
        if (now < dNDStartTime) {
            now += 2400;
        }
        return now < dNDStartTime || now >= dNDStopTime || !PreferenceUtils.isDND();
    }
}
