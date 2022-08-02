package com.security.applock.model;

public class FiveMinutesLock {
    private String packageName;
    private long time;

    public FiveMinutesLock(String packageName, long time) {
        this.packageName = packageName;
        this.time = time;
    }

    public FiveMinutesLock() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

