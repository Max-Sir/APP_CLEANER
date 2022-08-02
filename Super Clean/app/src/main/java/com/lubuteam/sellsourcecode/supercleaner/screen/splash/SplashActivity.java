package com.lubuteam.sellsourcecode.supercleaner.screen.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.main.MainActivity;
import com.lubuteam.sellsourcecode.supercleaner.service.NotificationUtil;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
      //  YoYo.with(Techniques.SlideInUp).duration(2000).playOn(tvContent);
        new Handler().postDelayed(() -> {
            handlerScreen();
        }, 4000);

        // for tests notifications
//        NotificationUtil.getInstance().showNotificationBatteryFull(this);
//        NotificationUtil.getInstance().showNotificationAlarm(this, NotificationUtil.ID_NOTIFICATTION_PHONE_BOOST);
//        NotificationUtil.getInstance().showNotificationAlarm(this, NotificationUtil.ID_NOTIFICATTION_CPU_COOLER);
//        NotificationUtil.getInstance().showNotificationAlarm(this, NotificationUtil.ID_NOTIFICATTION_BATTERY_SAVE);
//        NotificationUtil.getInstance().showNotificationAlarm(this, NotificationUtil.ID_NOTIFICATTION_JUNK_FILE);

//        NotificationUtil.getInstance().showNotificationInstallUninstallApk(this, "com.lubuteam.sellsourcecode.supercleaner", NotificationUtil.ID_NOTIFICATTION_UNINSTALL);
//        NotificationUtil.getInstance().showNotificationInstallUninstallApk(this, "com.lubuteam.sellsourcecode.supercleaner", NotificationUtil.ID_NOTIFICATTION_INSTALL);

//        NotificationUtil.getInstance().postNotificationHide(this, null);
//        NotificationUtil.getInstance().postNotificationSpam(null, 2);

    }

    private void handlerScreen() {
        Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }
}
