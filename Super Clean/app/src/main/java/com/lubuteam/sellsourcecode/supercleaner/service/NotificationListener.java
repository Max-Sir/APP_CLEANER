package com.lubuteam.sellsourcecode.supercleaner.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.lubuteam.sellsourcecode.supercleaner.AppClean;
import com.lubuteam.sellsourcecode.supercleaner.listener.ObserverPartener.ObserverUtils;
import com.lubuteam.sellsourcecode.supercleaner.listener.ObserverPartener.eventModel.EvbAddListNoti;
import com.lubuteam.sellsourcecode.supercleaner.model.NotifiModel;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;
import com.lubuteam.sellsourcecode.supercleaner.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationListener extends NotificationListenerService {

    private Timer timer;
    private String currentPakage = "";
    private ActivityManager activityManager;
    private PackageManager packageManager;
    private static NotificationListener listenerNotifi;
    private List<NotifiModel> lstSave = new ArrayList<>();

    public static NotificationListener getInstance() {
        return listenerNotifi;
    }

    public List<NotifiModel> getLstSave() {
        return lstSave;
    }

    private static void setInstance(NotificationListener instance) {
        NotificationListener.listenerNotifi = instance;
    }

    public void removeItemLst(int position) {
        if (lstSave.size() > position)
            lstSave.remove(position);
        if (lstSave.size() != 0 && SystemUtil.checkDNDDoing())
            NotificationUtil.getInstance().postNotificationSpam(lstSave.get(0).barNotification, lstSave.size());
        else
            NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFI_CLEAN);
    }

    public void removeAllItem() {
        lstSave.clear();
        NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFI_CLEAN);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!getPackageName().equalsIgnoreCase(sbn.getPackageName())) {
            if (PreferenceUtils.isTurnOnCleanNotification()) {
                if (lstSave.isEmpty()) {
                    loadCurrentNotifi();
                } else {
                    List<StatusBarNotification> lst = new ArrayList<>();
                    lst.add(sbn);
                    if (isPkgClean(sbn.getPackageName())) {
                        sendData(lst);
                    }
                }
            } else if (PreferenceUtils.isHideNotification()) {
                String pkg = sbn.getPackageName();
                if (checkIsMuteNoti(pkg, PreferenceUtils.getListAppGameBoost())) {
                    NotificationUtil.getInstance().postNotificationHide(AppClean.getInstance(), sbn);
                    NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFI_HIDE);
                }
            }
        }
    }

    private void cancelNotification(StatusBarNotification sbn) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
        } else {
            cancelNotification(sbn.getKey());
        }
    }

    public void onCreate() {
        super.onCreate();
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                String pagekageName = SystemUtil.getLauncherTopApp(NotificationListener.this, activityManager);
                if (pagekageName != null && !pagekageName.equals("") && !currentPakage.equals(pagekageName)) {
                    currentPakage = pagekageName;
                }
            }
        }, 0, 2000);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    public boolean checkIsMuteNoti(String pkgNoti, List<String> lstPkgSave) {
        if (lstPkgSave.isEmpty())
            return false;
        for (String pkgName : lstPkgSave) {
            if (pkgName.equals(currentPakage) && !pkgNoti.equals(currentPakage))
                return true;
        }
        return false;
    }

    public void sendData(List<StatusBarNotification> lst) {
        if (lst != null && lst.size() > 0) {
            List<NotifiModel> lstNoti = new ArrayList<>();
            for (StatusBarNotification mBarNotification : lst) {
                cancelNotification(mBarNotification);
                try {
                    String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(mBarNotification.getPackageName(), PackageManager.GET_META_DATA));
                    Drawable icon = packageManager.getApplicationIcon(packageManager.getApplicationInfo(mBarNotification.getPackageName(), PackageManager.GET_META_DATA));
                    NotifiModel notifiModel = new NotifiModel(appName, icon, mBarNotification);
                    lstNoti.add(notifiModel);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (lstNoti.size() == 1) {
                NotifiModel modelAdd = lstNoti.get(0);
                for (int k = lstSave.size() - 1; k >= 0; k--) {
                    NotifiModel notifiModel = lstSave.get(k);
                    if (notifiModel.barNotification.getId() == modelAdd.barNotification.getId()) {
                        lstSave.remove(k);
                    }
                }
                lstSave.add(0, modelAdd);
            } else {
                lstSave.clear();
                lstSave.addAll(lstNoti);
            }
            if (SystemUtil.checkDNDDoing())
                NotificationUtil.getInstance().postNotificationSpam(lst.get(0), lstSave.size());
            ObserverUtils.getInstance().notifyObservers(new EvbAddListNoti(lstSave));
        }
    }

    public boolean isPkgClean(String pkgName) {
        List<String> lstSave = PreferenceUtils.getListAppCleanNotifi();
        if (lstSave.isEmpty())
            return false;
        for (String pkg : lstSave) {
            if (pkg.equalsIgnoreCase(pkgName))
                return true;
        }
        return false;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        packageManager = getPackageManager();
        setInstance(NotificationListener.this);
        loadCurrentNotifi();
    }

    public void loadCurrentNotifi() {
        new loadCurrentNotifi().execute();
    }

    public class loadCurrentNotifi extends AsyncTask<Void, Void, StatusBarNotification[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lstSave.clear();
        }

        @Override
        protected StatusBarNotification[] doInBackground(Void... voids) {
            try {
                return getActiveNotifications();
            } catch (Exception e) {

            }
            return new StatusBarNotification[]{};
        }

        @Override
        protected void onPostExecute(StatusBarNotification[] statusBarNotifications) {
            super.onPostExecute(statusBarNotifications);
            if (statusBarNotifications.length != 0) {
                List<StatusBarNotification> lstTmp = new ArrayList<>();
                if (PreferenceUtils.isTurnOnCleanNotification()) {
                    for (StatusBarNotification mNotification : statusBarNotifications) {
                        if (isPkgClean(mNotification.getPackageName()) && !getPackageName().equalsIgnoreCase(mNotification.getPackageName())) {
                            lstTmp.add(mNotification);
                        }
                    }
                    sendData(lstTmp);
                }
            }
        }
    }

}
