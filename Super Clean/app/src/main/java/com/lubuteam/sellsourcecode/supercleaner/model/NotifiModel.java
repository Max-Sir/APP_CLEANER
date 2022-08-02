package com.lubuteam.sellsourcecode.supercleaner.model;

import android.graphics.drawable.Drawable;
import android.service.notification.StatusBarNotification;

import java.io.Serializable;

public class NotifiModel implements Serializable {
    public String appName;
    public Drawable iconApp;
    public StatusBarNotification barNotification;

    public NotifiModel(String appName, Drawable iconApp, StatusBarNotification barNotification) {
        this.appName = appName;
        this.iconApp = iconApp;
        this.barNotification = barNotification;
    }

    public NotifiModel(StatusBarNotification barNotification) {
        this.barNotification = barNotification;
    }

}
