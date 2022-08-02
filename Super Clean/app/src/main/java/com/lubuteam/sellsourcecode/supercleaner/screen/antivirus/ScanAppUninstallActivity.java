package com.lubuteam.sellsourcecode.supercleaner.screen.antivirus;

import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.ExitActivity;
import com.lubuteam.sellsourcecode.supercleaner.service.NotificationUtil;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanAppUninstallActivity extends BaseActivity {

    @BindView(R.id.ll_deleting)
    View llDeleting;
    @BindView(R.id.ll_content)
    View llContent;
    @BindView(R.id.ic_fan)
    ImageView imFan;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private String pkgName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiti_app_uninstall);
        ButterKnife.bind(this);
        NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFICATTION_UNINSTALL);
        initData();
    }

    private void initData() {
        pkgName = getIntent().getStringExtra(Config.PKG_RECERVER_DATA);
        String txt = "<b>" + pkgName + "</b>" + " " + getString(R.string.remove_app_unstall);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvContent.setText(Html.fromHtml(txt, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvContent.setText(Html.fromHtml(txt));
        }
    }

    @OnClick({R.id.tv_cancel, R.id.tv_clean, R.id.layout_padding})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.tv_cancel:
                ExitActivity.exitApplicationAndRemoveFromRecent(this);
                break;
            case R.id.tv_clean:
                cleanCache(pkgName);
                llDeleting.setVisibility(View.VISIBLE);
                llContent.setVisibility(View.GONE);
                RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(400);
                rotate.setFillAfter(true);
                rotate.setRepeatCount(Animation.INFINITE);
                rotate.setInterpolator(new LinearInterpolator());
                imFan.startAnimation(rotate);
                new Handler().postDelayed(() -> {
                    openScreenResult(null);
                    finish();
                }, 2000);
                break;
        }
    }

    public void cleanCache(String pkgName) {
        PackageManager mPackageManager = getPackageManager();
        try {
            Method mGetfreeStorage = getPackageManager().getClass().getMethod(
                    "freeStorage", String.class, IPackageStatsObserver.class);
            long desiredFreeStorage = 8 * 1024 * 1024 * 1024; // Request for 8GB of free space
            mGetfreeStorage.invoke(mPackageManager, pkgName, desiredFreeStorage, null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
