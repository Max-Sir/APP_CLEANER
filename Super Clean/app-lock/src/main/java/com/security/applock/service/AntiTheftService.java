package com.security.applock.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.security.applock.service.notification.ServiceNotificationManager;
import com.security.applock.service.receiver.ScreenReceiver;
import com.security.applock.service.receiver.WatchmenReceiver;
import com.security.applock.ui.main.MainActivity;
import com.security.applock.ui.password.PasswordActivity;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;
import com.security.applock.utils.Toolbox;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AntiTheftService extends Service implements ServiceNotificationManager.ClickNotificationListener {

    private ScreenReceiver screenOnOffReceiver;
    private LockManager lockManager;
    private Runnable runnable;
    private Handler handler;
    private ServiceNotificationManager serviceNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeAppLockerNotification();
        initLockManager();
        registerBroastcastReceiver();
        handler = new Handler();
        runLockApp();
        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                lockManager.showhideLockView(false, "");
            }

            @Override
            public void onHomeLongPressed() {
                lockManager.showhideLockView(false, "");
            }
        });
        mHomeWatcher.startWatch();
    }

    private void initLockManager() {
        lockManager = new LockManager(this);
    }

    private void registerBroastcastReceiver() {
        /** Sự kiện on off màn hình*/
        if (screenOnOffReceiver == null) {
            screenOnOffReceiver = new ScreenReceiver();
            screenOnOffReceiver.setScreenCallback(new ScreenReceiver.ScreenCallback() {

                @Override
                public void onScrenOn() {
                    handler.removeCallbacks(runnable);
                    handler.post(runnable);
                }

                @Override
                public void onScrenOff() {
                    handler.removeCallbacks(runnable);
                    PreferencesHelper.setListAppScreenOff(new ArrayList<>());
                }
            });
            screenOnOffReceiver.onCreate(this);
        }
    }

    private void initializeAppLockerNotification() {
        serviceNotificationManager = ServiceNotificationManager.getInstance(this);
        serviceNotificationManager.setClickNotificationListener(this);
        startForeground(ServiceNotificationManager.NOTIFICATION_ID_SERVICE
                , serviceNotificationManager.showNotification());
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationManager.deleteNotificationChannel(String.valueOf(ServiceNotificationManager.NOTIFICATION_ID_SERVICE));
//        }
        notificationManager.cancel(ServiceNotificationManager.NOTIFICATION_ID_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Config.ActionIntent.ACTION_SET_TIME_LOCK:
                    handler.removeCallbacks(runnable);
                    handler.post(runnable);
                    break;
                case Config.ActionIntent.ACTION_STOP_SERVICE:
                    checkStopService();
                    break;
                case Config.ActionIntent.ACTION_UPDATE_APP_MASK:
                    serviceNotificationManager.updateNotification();
                    break;
            }
        }
        return START_STICKY;
    }

    private void checkStopService() {
        if (!PreferencesHelper.getBoolean(PreferencesHelper.ENABLE_KIDZONE, false)) {
            forceStopSerivce();
        }
    }

    private void runLockApp() {
        runnable = () -> {
            lockManager.lockApp();
            handler.postDelayed(runnable, 200);
        };
        handler.post(runnable);
    }

    @Override
    public void onClickNotification(int id) {
        Intent intentAct = new Intent(this, MainActivity.class);
        intentAct.setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!Toolbox.isAppOnForeground(getApplicationContext())) {
            startActivity(intentAct);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lockManager != null) {
            lockManager.unregisterInstallUninstallReceiver();
        }
        handler.removeCallbacks(runnable);
        unregisterBoradcastReceiver();
        Intent intent = new Intent(this, WatchmenReceiver.class);
        sendBroadcast(intent);
    }

    private void forceStopSerivce() {
        onDestroy();
        stopSelf();
        stopForeground(true);
    }

    private void unregisterBoradcastReceiver() {
        /** hủy lẳng nghe sự kiện on off màn hình*/
        if (screenOnOffReceiver != null) {
            screenOnOffReceiver.onDestroy(this);
            screenOnOffReceiver = null;
        }
    }

    private void gotoCheckPasswordAntiTheft() {
        Intent intent = new Intent(this, PasswordActivity.class);
        intent.setAction(Config.ActionIntent.ACTION_CHECK_PASSWORD_ANTI_THEFT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class HomeWatcher {

        static final String TAG = "hg";
        private Context mContext;
        private IntentFilter mFilter;
        private OnHomePressedListener mListener;
        private InnerReceiver mReceiver;

        public interface OnHomePressedListener {
            void onHomePressed();

            void onHomeLongPressed();
        }

        public HomeWatcher(Context context) {
            mContext = context;
            mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        }

        public void setOnHomePressedListener(OnHomePressedListener listener) {
            mListener = listener;
            mReceiver = new InnerReceiver();
        }

        public void startWatch() {
            if (mReceiver != null) {
                mContext.registerReceiver(mReceiver, mFilter);
            }
        }

        public void stopWatch() {
            if (mReceiver != null) {
                mContext.unregisterReceiver(mReceiver);
            }
        }

        class InnerReceiver extends BroadcastReceiver {
            final String SYSTEM_DIALOG_REASON_KEY = "reason";
            final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
            final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
            final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                    String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                    if (reason != null) {
                        if (mListener != null) {
                            if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                                mListener.onHomePressed();
                            } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                                mListener.onHomeLongPressed();
                            }
                        }
                    }
                }
            }
        }
    }

}
