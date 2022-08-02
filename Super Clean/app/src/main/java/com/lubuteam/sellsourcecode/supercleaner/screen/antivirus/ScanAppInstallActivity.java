package com.lubuteam.sellsourcecode.supercleaner.screen.antivirus;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.data.TaskScanAntivirus;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.ExitActivity;
import com.lubuteam.sellsourcecode.supercleaner.service.NotificationUtil;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanAppInstallActivity extends BaseActivity {

    @BindView(R.id.av_scan)
    LottieAnimationView avScanView;
    @BindView(R.id.im_iconApp)
    ImageView imIconApp;
    @BindView(R.id.im_iconApp2)
    ImageView imIconApp2;
    @BindView(R.id.ll_content)
    View llContent;
    @BindView(R.id.ll_scan)
    View llScan;
    @BindView(R.id.tv_appname)
    TextView tvAppName;
    @BindView(R.id.tv_pkg_name)
    TextView tvPkgName;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_virus_name)
    TextView tvVirusName;
    @BindView(R.id.use_now)
    TextView tvUseNow;

    private String pkgName;
    private String virusName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiti_app_install);
        ButterKnife.bind(this);
        NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFICATTION_INSTALL);
        initView();
        try {
            initData();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        avScanView.setMaxFrame(140);
        avScanView.setMinFrame(20);
        avScanView.playAnimation();
        YoYo.with(Techniques.Flash).duration(2000).repeat(1000).playOn(imIconApp);
    }

    private void initData() throws PackageManager.NameNotFoundException {
        pkgName = getIntent().getStringExtra(Config.PKG_RECERVER_DATA);
        ApplicationInfo appInfo = getPackageManager().getApplicationInfo(pkgName, 0);
        PackageManager mPackageManager = getPackageManager();
        tvPkgName.setText(pkgName);
        tvAppName.setText(appInfo.loadLabel(mPackageManager).toString());
        imIconApp.setImageDrawable(appInfo.loadIcon(mPackageManager));
        imIconApp2.setImageDrawable(appInfo.loadIcon(mPackageManager));

        new TaskScanAntivirus(this, pkgName, 0, new TaskScanAntivirus.OnTaskListListener() {
            @Override
            public void OnResult(List<TaskInfo> lstDangerous, List<TaskInfo> lstVirus) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        avScanView.pauseAnimation();
                        llContent.setVisibility(View.VISIBLE);
                        llScan.setVisibility(View.INVISIBLE);
                        virusName = checkVirus(lstVirus);
                        if (virusName == null) {
                            tvContent.setText(getString(R.string.app_safe));
                        } else {
                            tvContent.setText(getString(R.string.app_virus_contain));
                            tvContent.setTextColor(getResources().getColor(R.color.color_D2221));
                            tvVirusName.setVisibility(View.VISIBLE);
                            tvVirusName.setText(virusName);
                            tvUseNow.setText(getString(R.string.uninstall));
                            tvUseNow.setTextColor(getResources().getColor(R.color.color_f62c2a));
                        }
                    }
                }, 2000);
            }

            @Override
            public void onProgress(String appName, String virusName, String dangerousSize, String progress) {

            }

        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public String checkVirus(List<TaskInfo> mList) {
        if (mList.isEmpty())
            return null;
        for (TaskInfo mTaskInfo : mList) {
            if (mTaskInfo.getPackageName().equals(pkgName))
                return mTaskInfo.getVirusName();
        }
        return null;
    }

    @OnClick({R.id.use_now, R.id.tv_ok})
    void click(View mView) {
        if (mView == null)
            return;
        try {
            ExitActivity.exitApplicationAndRemoveFromRecent(this);
            switch (mView.getId()) {
                case R.id.tv_ok:
                    break;
                case R.id.use_now:
                    if (virusName == null) {
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pkgName);
                        startActivity(launchIntent);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                        intent.setData(Uri.parse("package:" + pkgName));
                        startActivity(intent);
                    }
                    break;
            }
        } catch (Exception e) {

        }
    }
}
