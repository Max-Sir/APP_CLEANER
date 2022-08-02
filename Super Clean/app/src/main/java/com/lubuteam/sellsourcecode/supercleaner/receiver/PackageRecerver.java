package com.lubuteam.sellsourcecode.supercleaner.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.lubuteam.sellsourcecode.supercleaner.service.ServiceManager;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;

import java.io.File;


public class PackageRecerver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
            String pkgName = intent.getData().getSchemeSpecificPart();
            if (PreferenceUtils.isProtectionRealTime() && ServiceManager.getInstance() != null) {
                ServiceManager.getInstance().startInstall(pkgName);
            }
            if (PreferenceUtils.isScanInstaillApk()) {
                try {
                    ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(pkgName, 0);
                    File mFile = new File(appInfo.sourceDir);
                    mFile.delete();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String pkgName = intent.getData().getSchemeSpecificPart();
            if (PreferenceUtils.isScanUninstaillApk() && ServiceManager.getInstance() != null)
                ServiceManager.getInstance().startUninstall(pkgName);
        }
    }

    public final void OnCreate(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        context.registerReceiver(this, filter);
    }

    public final void OnDestroy(Context context) {
        if (context != null) {
            context.unregisterReceiver(this);
        }
    }
}
