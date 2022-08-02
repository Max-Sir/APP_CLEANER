package com.lubuteam.sellsourcecode.supercleaner.screen.gameboost;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsourcecode.supercleaner.AppClean;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.adapter.AppIconAdapter;
import com.lubuteam.sellsourcecode.supercleaner.data.KillAppProgress;
import com.lubuteam.sellsourcecode.supercleaner.data.TaskListBoost;
import com.lubuteam.sellsourcecode.supercleaner.data.TotalRamTask;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.listAppSelect.AppSelectActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.main.MainActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;
import com.lubuteam.sellsourcecode.supercleaner.utils.SystemUtil;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;
import com.lubuteam.sellsourcecode.supercleaner.widget.circularprogressindicator.CircularProgressIndicator;
import com.github.a.a.c;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameBoostActivity extends BaseActivity {

    @BindView(R.id.im_back_toolbar)
    ImageView imBackToolbar;
    @BindView(R.id.tv_toolbar)
    TextView tvTitleToolbar;
    @BindView(R.id.tv_ram_used)
    TextView tvRamUsed;
    @BindView(R.id.prg_ram_used)
    CircularProgressIndicator prgRamUsed;
    @BindView(R.id.rcv_game_boost)
    RecyclerView rcvGameBoost;
    @BindView(R.id.ll_animation_boost_game)
    RelativeLayout llAnimationBoost;
    @BindView(R.id.im_iconApp)
    ImageView imIconApp;
    @BindView(R.id.im_rocket_boost)
    ImageView imRocketBoost;
    @BindView(R.id.tv_number_app)
    TextView tvNumberApp;
    @BindView(R.id.cb_hide_notification)
    SwitchCompat cbHideNotification;

    private AppIconAdapter mAppIconAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_boost);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        imBackToolbar.setVisibility(View.VISIBLE);
        tvTitleToolbar.setText(getString(R.string.game_booster));
        tvNumberApp.setText(getString(R.string.game_add, String.valueOf(0)));
    }

    private void initData() {
        int randomMemnory = new Random().nextInt(20) + 30;
        tvRamUsed.setText(String.valueOf(randomMemnory));
        prgRamUsed.setCurrentProgress(randomMemnory);

        cbHideNotification.setChecked(SystemUtil.isNotificationListenerEnabled(this) && PreferenceUtils.isHideNotification());
        new TotalRamTask((useRam, totalRam) -> {
            prgRamUsed.setCurrentProgress(0);
            float progress = (float) useRam / (float) totalRam;
            if (tvRamUsed != null && prgRamUsed != null) {
                tvRamUsed.setText(String.valueOf((int) (progress * 100)));
                prgRamUsed.setCurrentProgress((int) (progress * 100));
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        loadAppBoostData();
    }

    private void loadAppBoostData() {
        List<String> lstPkgAppBoost = new ArrayList<>();
        lstPkgAppBoost.add(null);
        lstPkgAppBoost.addAll(PreferenceUtils.getListAppGameBoost());
        mAppIconAdapter = new AppIconAdapter(lstPkgAppBoost);
        mAppIconAdapter.setmOnClickItemListener(pkgName -> {
            if (pkgName == null) {
                AppSelectActivity.openSelectAppScreen(GameBoostActivity.this, AppSelectActivity.TYPE_SCREEN.GAME_BOOST);
            } else {
                startAnimationBoost(pkgName);
            }
        });
        rcvGameBoost.setAdapter(mAppIconAdapter);
        tvNumberApp.setText(getString(R.string.game_add, String.valueOf(lstPkgAppBoost.size() - 1)));
    }

    private void startAnimationBoost(String pkgSelect) {
        killAppRunning();
        try {
            Drawable icon = getPackageManager().getApplicationIcon(pkgSelect);
            imIconApp.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        llAnimationBoost.setVisibility(View.VISIBLE);
        c.a(imIconApp).a(10000).g(0.9f, 1.1f, 1.0f).b((300)).e();
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        imRocketBoost.startAnimation(rotate);
        new Handler().postDelayed(() -> {
            llAnimationBoost.setVisibility(View.GONE);
            imRocketBoost.clearAnimation();
            try {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pkgSelect);
                startActivity(launchIntent);
            } catch (Exception e) {
                Toast.makeText(GameBoostActivity.this, getString(R.string.app_not_exist), Toast.LENGTH_LONG).show();
            }
        }, 3000);
    }

    public void killAppRunning() {
        new TaskListBoost(new TaskListBoost.OnTaskListListener() {
            @Override
            public void OnResult(List<TaskInfo> arrList) {
                new KillAppProgress(GameBoostActivity.this, arrList).execute();
            }

            @Override
            public void onProgress(String appName) {

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAppIconAdapter != null && rcvGameBoost != null) {
            loadAppBoostData();
        }
    }

    @OnClick({R.id.tv_create_shortcut, R.id.view_checkbox})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.tv_create_shortcut:
                Toolbox.creatShorCutGameBoost(this);
                break;
            case R.id.view_checkbox:
                if (!cbHideNotification.isChecked()) {
                    try {
                        askPermissionUsageSetting(() -> {
                            askPermissionNotificaitonSetting(() -> {
                                cbHideNotification.setChecked(true);
                                PreferenceUtils.setHideNotification(true);
                                return null;
                            });
                            return null;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    PreferenceUtils.setHideNotification(false);
                    cbHideNotification.setChecked(false);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (AppClean.getInstance().getActivityList().size() == 1 || AppClean.getInstance().getActivityList().size() >= 3) {
            Intent mIntent = new Intent(this, MainActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(mIntent);
        } else {
            super.onBackPressed();
        }
    }
}
