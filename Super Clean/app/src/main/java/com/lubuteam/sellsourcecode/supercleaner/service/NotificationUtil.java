package com.lubuteam.sellsourcecode.supercleaner.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.IdRes;
import androidx.core.app.NotificationCompat;

import com.lubuteam.sellsourcecode.supercleaner.AppClean;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.screen.antivirus.ScanAppInstallActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.antivirus.ScanAppUninstallActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.main.MainActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;
import com.lubuteam.sellsourcecode.supercleaner.utils.SystemUtil;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;

import static androidx.core.app.NotificationCompat.EXTRA_TEXT;
import static androidx.core.app.NotificationCompat.EXTRA_TITLE;

public class NotificationUtil {

    public static final int ID_NOTIFI_MANAGER = 1000;
    public static final int ID_NOTIFI_HIDE = 10001;
    public static final int ID_NOTIFI_CLEAN = 10002;
    public static final int ID_NOTIFICATTION_FULL_BATTERY = 10003;
    public static final int ID_NOTIFICATTION_PHONE_BOOST = 10004;
    public static final int ID_NOTIFICATTION_CPU_COOLER = 10005;
    public static final int ID_NOTIFICATTION_BATTERY_SAVE = 10006;
    public static final int ID_NOTIFICATTION_JUNK_FILE = 10007;
    public static final int ID_NOTIFICATTION_INSTALL = 10008;
    public static final int ID_NOTIFICATTION_UNINSTALL = 10009;
    public static final String ACTION_NOTIFICATION_BUTTON_CLICK = "acction click notification";
    public static final String EXTRA_BUTTON_CLICKED = "extra click button";

    private static NotificationUtil instance;

    public NotificationUtil() {
    }

    public static synchronized NotificationUtil getInstance() {
        if (instance == null)
            return new NotificationUtil();
        return instance;
    }

    public void showNotificationBatteryFull(Context mContext) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(NotificationUtil.ID_NOTIFICATTION_FULL_BATTERY + "", name, importance);
            manager.createNotificationChannel(mChannel);
        }
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_notification_battery_full);
        Notification
                notification = new NotificationCompat.Builder(mContext, NotificationUtil.ID_NOTIFICATTION_FULL_BATTERY + "")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCustomContentView(remoteViews)
//                .setSmallIcon(R.drawable.ic_power_saving)
                .setSmallIcon(R.drawable.ic_charge_full_reminder)
                .build();
        manager.notify(ID_NOTIFICATTION_FULL_BATTERY, notification);
    }

    public void showNotificationAlarm(Context mContext, int notificationID) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_notification_alarm);
        int smallIcon = 0;
        if (notificationID == ID_NOTIFICATTION_PHONE_BOOST) {
            smallIcon = R.drawable.ic_notification_phone_booster;
            remoteViews.setTextViewText(R.id.tv_title, mContext.getString(R.string.ram_going_full));
            remoteViews.setTextViewText(R.id.tv_content, mContext.getString(R.string.use_phone_boost));
            remoteViews.setImageViewBitmap(R.id.im_icon, Toolbox.drawableToBitmap(mContext, mContext.getResources().getDrawable(R.drawable.ic_notification_phone_booster)));
            remoteViews.setTextViewText(R.id.tv_action, mContext.getString(R.string.optimize));
            resultIntent.putExtra(Config.ALARM_OPEN_FUNTION, Config.FUNCTION.PHONE_BOOST.id);
        } else if (notificationID == ID_NOTIFICATTION_CPU_COOLER) {
            smallIcon = R.drawable.ic_notification_cpu_cooler;
            remoteViews.setTextViewText(R.id.tv_title, mContext.getString(R.string.phone_is_hot));
            remoteViews.setTextViewText(R.id.tv_content, mContext.getString(R.string.use_cpu_cleaner));
            remoteViews.setImageViewBitmap(R.id.im_icon, Toolbox.drawableToBitmap(mContext, mContext.getResources().getDrawable(R.drawable.ic_notification_cpu_cooler)));
            remoteViews.setTextViewText(R.id.tv_action, mContext.getString(R.string.fix));
            resultIntent.putExtra(Config.ALARM_OPEN_FUNTION, Config.FUNCTION.CPU_COOLER.id);
        } else if (notificationID == ID_NOTIFICATTION_BATTERY_SAVE) {
            smallIcon = R.drawable.ic_notification_power_saving;
            remoteViews.setTextViewText(R.id.tv_title, mContext.getString(R.string.battery_low));
            remoteViews.setTextViewText(R.id.tv_content, mContext.getString(R.string.use_save_battery));
            remoteViews.setImageViewBitmap(R.id.im_icon, Toolbox.drawableToBitmap(mContext, mContext.getResources().getDrawable(R.drawable.ic_notification_power_saving)));
            remoteViews.setTextViewText(R.id.tv_action, mContext.getString(R.string.save));
            resultIntent.putExtra(Config.ALARM_OPEN_FUNTION, Config.FUNCTION.POWER_SAVING.id);
        } else if (notificationID == ID_NOTIFICATTION_JUNK_FILE) {
            smallIcon = R.drawable.ic_notification_junk_file;
            remoteViews.setTextViewText(R.id.tv_title, mContext.getString(R.string.detect_junk_file));
            remoteViews.setTextViewText(R.id.tv_content, mContext.getString(R.string.use_junk_file));
            remoteViews.setImageViewBitmap(R.id.im_icon, Toolbox.drawableToBitmap(mContext, mContext.getResources().getDrawable(R.drawable.ic_notification_junk_file)));
            remoteViews.setTextViewText(R.id.tv_action, mContext.getString(R.string.clean));
            resultIntent.putExtra(Config.ALARM_OPEN_FUNTION, Config.FUNCTION.JUNK_FILES.id);
        }
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, notificationID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(notificationID + "", name, importance);
            manager.createNotificationChannel(mChannel);
        }

        Notification
                notification = new NotificationCompat.Builder(mContext, notificationID + "")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCustomContentView(remoteViews)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(smallIcon)
                .build();
        manager.notify(notificationID, notification);
    }

    public void showNotificationInstallUninstallApk(Context mContext, String pkgName, int notificationID) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_notification_alarm);
        int smallIcon = 0;
        Intent resultIntent = null;
        if (notificationID == ID_NOTIFICATTION_UNINSTALL) {
            resultIntent = new Intent(mContext, ScanAppUninstallActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            resultIntent.putExtra(Config.PKG_RECERVER_DATA, pkgName);
            smallIcon = R.drawable.ic_notification_junk_file;
            remoteViews.setTextViewText(R.id.tv_title, mContext.getString(R.string.dectect_apk_uninstall));
            remoteViews.setTextViewText(R.id.tv_content, mContext.getString(R.string.conduct_cleanup));
            remoteViews.setImageViewBitmap(R.id.im_icon, Toolbox.drawableToBitmap(mContext, mContext.getResources().getDrawable(R.drawable.ic_notification_junk_file)));
            remoteViews.setViewVisibility(R.id.tv_action, View.GONE);
        } else if (notificationID == ID_NOTIFICATTION_INSTALL) {
            resultIntent = new Intent(mContext, ScanAppInstallActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            resultIntent.putExtra(Config.PKG_RECERVER_DATA, pkgName);
            smallIcon = R.drawable.ic_security_guide;
            remoteViews.setTextViewText(R.id.tv_title, mContext.getString(R.string.dectect_apk_install));
            remoteViews.setTextViewText(R.id.tv_content, mContext.getString(R.string.conduct_scan_virus));
            remoteViews.setImageViewBitmap(R.id.im_icon, Toolbox.drawableToBitmap(mContext, mContext.getResources().getDrawable(R.drawable.ic_security_guide)));
            remoteViews.setViewVisibility(R.id.tv_action, View.GONE);
        }
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, notificationID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(notificationID + "", name, importance);
            manager.createNotificationChannel(mChannel);
        }
        Notification
                notification = new NotificationCompat.Builder(mContext, notificationID + "")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCustomContentView(remoteViews)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(smallIcon)
                .build();
        manager.notify(notificationID, notification);
    }

    public void fullPower(Context mContext) {
        if (PreferenceUtils.checkTimeBatteryFull()) {
            PreferenceUtils.setTimeBatteryFull();
            if (SystemUtil.checkDNDDoing()) {
                showNotificationBatteryFull(mContext);
                SystemUtil.intSound(mContext);
            }
        }
    }

    public void postNotificationHide(Context context, StatusBarNotification sbn) {
        NotificationCompat.Builder ncb;
        try {
            String title = (String) sbn.getNotification().extras.get(EXTRA_TITLE);
            String contextdesc = (String) sbn.getNotification().extras.get(EXTRA_TEXT);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel channel = new NotificationChannel(context.getString(R.string.app_name), "message", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                nm.createNotificationChannel(channel);
                ncb = new NotificationCompat.Builder(context, context.getString(R.string.app_name));
            } else {
                ncb = new NotificationCompat.Builder(context);
            }
            if (ncb != null) {
                RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_notification_hide_normal);
                rv.setImageViewBitmap(R.id.icon_iv, Toolbox.loadAppIcon(context, sbn.getPackageName()));
                rv.setTextViewText(R.id.title_tv, title);
                rv.setTextViewText(R.id.content_tv, contextdesc);
                ncb.setContentIntent(sbn.getNotification().contentIntent);
                ncb.setContent(rv);
                ncb.setSmallIcon(R.drawable.ic_notifi_small_hide);
                ncb.setOngoing(false);
                ncb.setAutoCancel(true);
                ncb.setPriority(2);
                ncb.setTicker("");
                nm.notify(ID_NOTIFI_HIDE, ncb.build());
            }
        } catch (Exception e) {
        }
    }

    public void postNotificationSpam(StatusBarNotification sbn, int numberNotifi) {
        Context context = AppClean.getInstance();
        NotificationCompat.Builder ncb;
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.app_name), "message", NotificationManager.IMPORTANCE_LOW);
            channel.enableVibration(true);
            nm.createNotificationChannel(channel);
            ncb = new NotificationCompat.Builder(context, context.getString(R.string.app_name));
        } else {
            ncb = new NotificationCompat.Builder(context);
        }
        if (ncb != null) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_notification_clean);
            rv.setImageViewBitmap(R.id.icon_iv, Toolbox.loadAppIcon(context, sbn.getPackageName()));
            rv.setTextViewText(R.id.tv_content, numberNotifi + "");
            rv.setOnClickPendingIntent(R.id.tv_clean_spam,
                    onButtonNotificationClick(context, R.id.tv_clean_spam));
            ncb.setContent(rv);
            ncb.setSmallIcon(R.drawable.ic_notifi_small_hide);
            ncb.setAutoCancel(true);
            nm.notify(ID_NOTIFI_CLEAN, ncb.build());
        }
    }

    public void cancelNotificationClean(int id) {
        NotificationManager notificationManager = (NotificationManager) AppClean.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

    public PendingIntent onButtonNotificationClick(Context mContext, @IdRes int id) {
        Intent intent = new Intent(NotificationUtil.ACTION_NOTIFICATION_BUTTON_CLICK);
        intent.putExtra(NotificationUtil.EXTRA_BUTTON_CLICKED, id);
        return PendingIntent.getBroadcast(mContext, id, intent, 0);
    }


}
