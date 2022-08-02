package com.security.applock.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

public class KeepLiveAccessibilityService extends AccessibilityService {

    private static KeepLiveAccessibilityService instance = null;

    public static KeepLiveAccessibilityService getInstance() {
        return instance;
    }

    private void setInstance(KeepLiveAccessibilityService instance) {
        KeepLiveAccessibilityService.instance = instance;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (BackgroundManager.getInstance(this).canStartService()) {
            BackgroundManager.getInstance(this).startService(AntiTheftService.class);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null)
            setInstance(KeepLiveAccessibilityService.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public void onInterrupt() {

    }
}
