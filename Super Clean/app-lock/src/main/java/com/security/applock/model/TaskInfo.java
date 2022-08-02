package com.security.applock.model;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskInfo implements Serializable {

    private ApplicationInfo appinfo;
    public long mem;
    private PackagesInfo pkgInfo;
    private PackageManager pm;
    private ActivityManager.RunningAppProcessInfo runinfo;
    private String title;
    private String virusName;
    private boolean chceked;
    private boolean isClickEnable = true;

    public TaskInfo() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskInfo taskInfo = (TaskInfo) o;
        return taskInfo.appinfo.packageName.equals(appinfo.packageName);
    }

    @Override
    public int hashCode() {
        int result = appinfo != null ? appinfo.hashCode() : 0;
        result = 31 * result + (int) (mem ^ (mem >>> 32));
        result = 31 * result + (pkgInfo != null ? pkgInfo.hashCode() : 0);
        result = 31 * result + (pm != null ? pm.hashCode() : 0);
        result = 31 * result + (runinfo != null ? runinfo.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (virusName != null ? virusName.hashCode() : 0);
        result = 31 * result + (chceked ? 1 : 0);
        result = 31 * result + (isClickEnable ? 1 : 0);
        return result;
    }

    public TaskInfo(Context context,
                    ActivityManager.RunningAppProcessInfo runinfo) {

        this.appinfo = null;
        this.pkgInfo = null;
        this.title = null;
        this.runinfo = runinfo;
        this.pm = context.getApplicationContext()
                .getPackageManager();
    }

    public TaskInfo(Context context, ApplicationInfo appinfo) {

        this.appinfo = null;
        this.pkgInfo = null;
        this.runinfo = null;
        this.title = null;
        this.appinfo = appinfo;
        this.pm = context.getApplicationContext()
                .getPackageManager();
    }

    public void getAppInfo() {
        if (appinfo == null) {
            try {
                String s = runinfo.processName;
                this.appinfo = pm.getApplicationInfo(s, 128);
            } catch (Exception e) {
            }
        }
    }

    public int getIcon() {
        return appinfo.icon;
    }

    public String getPackageName() {
        return appinfo.packageName;
    }

    public String getTitle() {

        if (title == null) {
            try {
                this.title = appinfo.loadLabel(pm).toString();
            } catch (Exception e) {

            }
        }
        return title;
    }

    public void setMem(long mem) {
        this.mem = mem;
    }

    public long getMem() {
        return mem;
    }

    public boolean isGoodProcess() {

        if (appinfo != null) {
            return true;
        } else {
            return false;
        }
    }

    public ApplicationInfo getAppinfo() {
        return appinfo;
    }

    public void setAppinfo(ApplicationInfo appinfo) {
        this.appinfo = appinfo;
    }

    public PackagesInfo getPkgInfo() {
        return pkgInfo;
    }

    public void setPkgInfo(PackagesInfo pkgInfo) {
        this.pkgInfo = pkgInfo;
    }

    public ActivityManager.RunningAppProcessInfo getRuninfo() {
        return runinfo;
    }

    public void setRuninfo(ActivityManager.RunningAppProcessInfo runinfo) {
        this.runinfo = runinfo;
    }

    public boolean isChceked() {
        return chceked;
    }

    public void setChceked(boolean chceked) {
        this.chceked = chceked;
    }

    public boolean isClickEnable() {
        return isClickEnable;
    }

    public void setClickEnable(boolean clickEnable) {
        isClickEnable = clickEnable;
    }


    public String getVirusName() {
        return virusName;
    }

    public void setVirusName(String virusName) {
        this.virusName = virusName;
    }
}
