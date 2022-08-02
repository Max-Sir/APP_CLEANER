package com.lubuteam.sellsourcecode.supercleaner.model;

import android.graphics.drawable.Drawable;

public class ChildItem {
    public static final int TYPE_APKS = 0;
    public static final int TYPE_CACHE = 1;
    public static final int TYPE_DOWNLOAD_FILE = 2;
    public static final int TYPE_LARGE_FILES = 3;

    private long mCacheSize;
    private String mPackageName;
    private String mApplicationName;
    private Drawable mIcon;
    private int type;
    private String path;
    private boolean isCheck;

    public ChildItem(String packageName, String applicationName,
                     Drawable icon, long cacheSize, int type,
                     String path, boolean isCheck) {
        mCacheSize = cacheSize;
        mPackageName = packageName;
        mApplicationName = applicationName;
        mIcon = icon;
        this.type = type;
        this.isCheck = isCheck;
        this.path = path;
    }

    public Drawable getApplicationIcon() {
        return mIcon;
    }

    public String getApplicationName() {
        return mApplicationName;
    }

    public long getCacheSize() {
        return mCacheSize;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
