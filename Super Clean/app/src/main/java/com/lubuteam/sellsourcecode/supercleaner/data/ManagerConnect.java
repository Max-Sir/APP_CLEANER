package com.lubuteam.sellsourcecode.supercleaner.data;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class ManagerConnect {

    private List<ApplicationInfo> mDatas = new ArrayList<>();

    public void getListManager(final Context context,
                               final OnManagerConnectListener listener) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mDatas = checkForLaunchIntent(context, context.getPackageManager().
                        getInstalledApplications(PackageManager.GET_META_DATA));
                if (listener != null) {
                    listener.OnResultManager(mDatas);
                }
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    private List<ApplicationInfo> checkForLaunchIntent(Context context, List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<>();
        for (ApplicationInfo info : list) {
            try {
                if (null != context.getPackageManager().getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applist;
    }

    public interface OnManagerConnectListener {
        void OnResultManager(List<ApplicationInfo> result);
    }
}

