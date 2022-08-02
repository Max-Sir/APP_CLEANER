package com.lubuteam.sellsourcecode.supercleaner.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.content.ContextCompat;

import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.window.DeepboostWindowmanager;

import java.util.ArrayList;
import java.util.List;

public class ForceStopAccessibility extends AccessibilityService {


    private enum ACTION {
        DO_NOTHING, PRESS_OK, PRESS_BACK, PRESS_FORCE_STOP
    }

    private long lastActionTimestamp = 0L;
    private static ForceStopAccessibility instance = null;
    private ACTION nextAction = ACTION.DO_NOTHING;
    private DeepboostWindowmanager mDeepboostWindowmanager;

    private static void setInstance(ForceStopAccessibility instance) {
        ForceStopAccessibility.instance = instance;
    }

    public static ForceStopAccessibility getInstance() {
        return instance;
    }

    public void startAutoClick() {
        if (nextAction == ACTION.DO_NOTHING) {
            nextAction = ACTION.PRESS_FORCE_STOP;
            lastActionTimestamp = System.currentTimeMillis();
        }
    }

    public void showHideViewMark(List<TaskInfo> lstApp) {
        if (mDeepboostWindowmanager != null)
            mDeepboostWindowmanager.startAnimation(lstApp);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {
            Intent mIntent = new Intent(this, ServiceManager.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(this, mIntent);
            } else {
                this.startService(new Intent(this, ServiceManager.class));
            }
            if (event == null)
                return;
            if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event
                    .getEventType()) {
                switch (nextAction) {
                    case DO_NOTHING:
                        break;
                    case PRESS_FORCE_STOP:
                        if (event.getClassName().equals("com.android.settings.applications.InstalledAppDetailsTop")) {
                            clickForceStopButton(event);
                        } else {
                            wrongScreenShown();
                        }
                        break;
                    case PRESS_OK:
                        if (event.getClassName().equals("android.app.AlertDialog") || event.getClassName().equals("androidx.appcompat.app.AlertDialog")) {
                            clickOkButton(event);
                        } else {
                            wrongScreenShown();
                        }
                        break;
                    case PRESS_BACK:
                        clickBackButton();
                        break;
                }
            }
        } catch (Exception e) {

        }
    }

    private void wrongScreenShown() {
        if (System.currentTimeMillis() - lastActionTimestamp > 8000) {
            nextAction = ACTION.DO_NOTHING;
        }

    }

    private boolean clickAll(List<AccessibilityNodeInfo> nodes) {
        if (nodes.isEmpty()) {
            nextAction = ACTION.DO_NOTHING;
            return false;
        }
        List<AccessibilityNodeInfo> lstNodeCanClick = new ArrayList<>();
        for (AccessibilityNodeInfo nodeInfo : nodes) {
            if (nodeInfo.isEnabled() && nodeInfo.isClickable())
                lstNodeCanClick.add(nodeInfo);
        }
        if (lstNodeCanClick.isEmpty()) {
            clickBackButton();
            return false;
        }

        for (AccessibilityNodeInfo nodeInfo : lstNodeCanClick) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        return true;
    }

    public void clickForceStopButton(AccessibilityEvent mEvent) {
        AccessibilityNodeInfo nodeInfo = mEvent.getSource();
        if (nodeInfo == null)
            return;
        List<AccessibilityNodeInfo> nodeToClick = nodeInfo.findAccessibilityNodeInfosByText("FORCE STOP");
        if (nodeToClick.isEmpty())
            nodeToClick = nodeInfo.findAccessibilityNodeInfosByViewId("com.android.settings:id/force_stop_button");
        if (nodeToClick.isEmpty())
            nodeToClick = nodeInfo.findAccessibilityNodeInfosByText(forceStopButtonName());
        if (nodeToClick.isEmpty())
            nodeToClick = nodeInfo.findAccessibilityNodeInfosByText("com.android.settings:id/right_button");

        boolean success = clickAll(nodeToClick);
        if (success)
            nextAction = ACTION.PRESS_OK;
        nodeInfo.recycle();
    }

    public void clickOkButton(AccessibilityEvent mEvent) {
        AccessibilityNodeInfo nodeInfo = mEvent.getSource();
        if (nodeInfo == null)
            return;
        List<AccessibilityNodeInfo> nodeToClick = nodeInfo.findAccessibilityNodeInfosByText(getString(android.R.string.ok));
        if (nodeToClick.isEmpty())
            nodeToClick = nodeInfo.findAccessibilityNodeInfosByText(forceStopButtonName());
        boolean success = clickAll(nodeToClick);
        if (success)
            nextAction = ACTION.PRESS_BACK;
        nodeInfo.recycle();
    }

    public String forceStopButtonName() {
        try {
            String resourcesPackageName = "com.android.settings";
            Resources resources = getApplicationContext().getPackageManager().getResourcesForApplication(resourcesPackageName);
            int resourceId = resources.getIdentifier("force_stop", "string", resourcesPackageName);
            if (resourceId > 0) {
                return resources.getString(resourceId);
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clickBackButton() {
        nextAction = ACTION.DO_NOTHING;
//        android.os.Process.killProcess(android.os.Process.myPid());
        performGlobalAction(GLOBAL_ACTION_BACK);
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        nextAction = ACTION.DO_NOTHING;
        if (instance == null)
            setInstance(ForceStopAccessibility.this);
        mDeepboostWindowmanager = new DeepboostWindowmanager(this);
        Log.i("XXX", "ACC::onServiceConnected: ");
    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub

    }
}