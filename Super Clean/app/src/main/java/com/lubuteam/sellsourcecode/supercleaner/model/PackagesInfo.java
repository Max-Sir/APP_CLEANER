package com.lubuteam.sellsourcecode.supercleaner.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.List;

public class PackagesInfo {
    private List appList;

    public PackagesInfo(Context context) {
        this.appList = context.getApplicationContext().getPackageManager()
                .getInstalledApplications(0);
    }

    public PackagesInfo(Context context, String s) {
        this.appList = context.getApplicationContext().getPackageManager()
                .getInstalledApplications(128);
    }

    public ApplicationInfo getInfo(String s) {

        ApplicationInfo applicationInfo = null;
        if (s != null) {
            for (Object anAppList : appList) {
                applicationInfo = (ApplicationInfo) anAppList;
                String s1 = applicationInfo.processName;
                if (s.equals(s1)) {
                    break;
                }
            }
        }

        return applicationInfo;
    }
}
