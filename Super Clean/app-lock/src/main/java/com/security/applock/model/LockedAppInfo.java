package com.security.applock.model;

import android.graphics.drawable.Drawable;

public class LockedAppInfo {
    private String packageName;
    private Drawable icon;

    public LockedAppInfo(String title, Drawable icon) {
        this.packageName = title;
        this.icon = icon;
    }

    public LockedAppInfo() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}

