package com.security.applock.service;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.security.applock.model.FiveMinutesLock;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;
import com.security.applock.utils.ViewUtils;
import com.security.applock.widget.LockViewWindowManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LockManager {
    private Context context;
    private BroadcastReceiver installUninstallReceiver;
    private String lastPackage = "";
    private UsageStatsManager usageStatsManager;
    private ActivityManager activityManager;
    private WindowManager windowManager;
    private LockViewWindowManager lockViewWindowManager;
    // 5 minutes
    private final int TIME_LOCK = 300 * 1000;

    public LockManager(Context context) {
        this.context = context;
        this.usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        initWindowManager();
        initReceiver();
        registerInstallUninstallReceiver();
    }

    private void initWindowManager() {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        lockViewWindowManager = new LockViewWindowManager(context);
        lockViewWindowManager.setPasswordConfirmListener(new LockViewWindowManager.PasswordConfirmListener() {
            @Override
            public void onSuccess(String currentPackage) {
                showhideLockView(false, "");
                if (PreferencesHelper.getInt(PreferencesHelper.SETTING_VALUE_TIME_LOCK, 1) == 3) {
                    List<String> list = PreferencesHelper.getListAppScreenOff();
                    list.add(currentPackage);
                    PreferencesHelper.setListAppScreenOff(list);
                } else if (PreferencesHelper.getInt(PreferencesHelper.SETTING_VALUE_TIME_LOCK, 1) == 2) {
                    List<FiveMinutesLock> fiveMinutesAppList = PreferencesHelper.getListApp5Minutes();
                    int positionPackage = -1;
                    loop:
                    for (int i = 0; i < fiveMinutesAppList.size(); i++) {
                        if (fiveMinutesAppList.get(i).getPackageName().equals(currentPackage)) {
                            positionPackage = i;
                            break loop;
                        }
                    }
                    if (positionPackage == -1) {
                        fiveMinutesAppList.add(new FiveMinutesLock(currentPackage, new Date().getTime()));
                    } else {
                        fiveMinutesAppList.get(positionPackage).setTime(new Date().getTime());
                    }
                    PreferencesHelper.setListApp5Minutes(fiveMinutesAppList);
                }
            }

            @Override
            public void onFails(String passInput) {

            }
        });
    }

    void showhideLockView(boolean isShow, String packageName) {
//        WindowManager.LayoutParams paramsViewContent = ViewUtils.setupLayoutParams();

        if (isShow) {
            lockViewWindowManager.showhideViewPassword(true, packageName);
            windowManager.addView(lockViewWindowManager, ViewUtils.setupLayoutParams());
        } else {
            try {
                windowManager.removeView(lockViewWindowManager);
            } catch (Exception e) {

            }
        }
    }

    private void initReceiver() {
        installUninstallReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (PreferencesHelper.getBoolean(PreferencesHelper.SETTING_NEW_APP_LOCK, false)) {
                    final String action = intent.getAction();
                    if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                        Uri data = intent.getData();
                        String pkgName = data.getEncodedSchemeSpecificPart();
                        List<String> list = PreferencesHelper.getListAppKidZone();
                        list.add(pkgName);
                        PreferencesHelper.setListAppKidZone(list);
                        Intent mIntent = new Intent(Config.ActionIntent.ACTION_NEW_APP_RECEIVER);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(mIntent);
                    }
                }
            }
        };
    }

    public void lockApp() {
        if (PreferencesHelper.getBoolean(PreferencesHelper.ENABLE_KIDZONE, false))
            onAppForeground(getLastPackage());
    }

    private String getLastPackage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getPackageHigherLollipop();
        } else {
            return getPackageLowerLollipop();
        }
    }

    private String getPackageHigherLollipop() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } else {
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                }
            }
            if (!TextUtils.isEmpty(result)) {
                return result;
            }
        }
        return "";
    }

    private String getPackageLowerLollipop() {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName componentName = mActivityManager.getRunningTasks(1).get(0).topActivity;
        if (componentName != null && componentName.getPackageName() != null) {
            return componentName.getPackageName();
        }
        return "";
    }

//    private void onAppForeground(String foregroundAppPackage) {
//        Log.d("TAG", "onAppForeground: " + foregroundAppPackage);
//        if (context == null || TextUtils.isEmpty(foregroundAppPackage)) return;
//        List<String> listAppLocked = PreferencesHelper.getListAppKidZone();
//        List<String> listHomeLaucher = getHomesLauncher();
//        // add when installer app
//        listAppLocked.add("com.android.packageinstaller");
//        if (listAppLocked.contains(foregroundAppPackage) &&
//                !listHomeLaucher.contains(foregroundAppPackage) &&
//                !lastPackage.equals(foregroundAppPackage)) {
//            Intent intent = new Intent(context, PasswordActivity.class);
//            intent.setAction(Config.ActionIntent.ACTION_CHECK_PASSWORD_FROM_SERVICE);
//            intent.putExtra(Config.KeyBundle.KEY_PACKAGE_NAME, foregroundAppPackage);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivityAllStage(context, intent);
//            lastPackage = foregroundAppPackage;
//        }
//        if (TextUtils.isEmpty(foregroundAppPackage) || listHomeLaucher.contains(foregroundAppPackage))
//            lastPackage = foregroundAppPackage;
//    }

    private void onAppForeground(String foregroundAppPackage) {
        Log.d("TAG", "onAppForeground: " + foregroundAppPackage);
        if (context == null || TextUtils.isEmpty(foregroundAppPackage)) return;
        List<String> listAppLocked = PreferencesHelper.getListAppKidZone();
        List<String> listHomeLaucher = getHomesLauncher();
        // add when installer app
        listAppLocked.add("com.android.packageinstaller");
        if (TextUtils.isEmpty(foregroundAppPackage)) {
            return;
        }
        if (!listHomeLaucher.contains(foregroundAppPackage)) {
            if (listAppLocked.contains(foregroundAppPackage)) {
                if (!lastPackage.equals(foregroundAppPackage)) {
                    checkTimeLockApp(foregroundAppPackage);
                }
            }
        } else {
            showhideLockView(false, "");
        }
        lastPackage = foregroundAppPackage;
    }

    private void checkTimeLockApp(String foregroundAppPackage) {
        if (PreferencesHelper.getInt(PreferencesHelper.SETTING_VALUE_TIME_LOCK, 1) == 3) {
            List<String> offAppList = PreferencesHelper.getListAppScreenOff();
            if (!offAppList.contains(foregroundAppPackage)) {
                showhideLockView(true, foregroundAppPackage);
            }
        } else if (PreferencesHelper.getInt(PreferencesHelper.SETTING_VALUE_TIME_LOCK, 1) == 2) {
            List<FiveMinutesLock> fiveMinutesAppList = PreferencesHelper.getListApp5Minutes();
            int positionPackage = -1;
            for (int i = 0; i < fiveMinutesAppList.size(); i++) {
                if (fiveMinutesAppList.get(i).getPackageName().equals(foregroundAppPackage)) {
                    positionPackage = i;
                    break;
                }
            }
            if (positionPackage == -1) {
                showhideLockView(true, foregroundAppPackage);
            } else if (new Date().getTime() - fiveMinutesAppList.get(positionPackage).getTime() >= TIME_LOCK) {
                showhideLockView(true, foregroundAppPackage);
            }
        } else {
            showhideLockView(true, foregroundAppPackage);
        }
    }

    private List<String> getHomesLauncher() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        names.add(packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName);
        return names;
    }

    public void registerInstallUninstallReceiver() {
        if (context != null) {
            IntentFilter installUninstallFilter = new IntentFilter();
            installUninstallFilter.addAction(Intent.ACTION_INSTALL_PACKAGE);
            installUninstallFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            installUninstallFilter.addDataScheme("package");
            context.registerReceiver(installUninstallReceiver, installUninstallFilter);
        }
    }

    public void unregisterInstallUninstallReceiver() {
        if (context != null) {
            try {
                context.unregisterReceiver(installUninstallReceiver);
            } catch (Exception e) {

            }
        }
    }
}

