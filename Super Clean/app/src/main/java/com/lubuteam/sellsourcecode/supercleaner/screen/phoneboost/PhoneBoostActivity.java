package com.lubuteam.sellsourcecode.supercleaner.screen.phoneboost;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.adapter.AppSelectAdapter;
import com.lubuteam.sellsourcecode.supercleaner.data.KillAppProgress;
import com.lubuteam.sellsourcecode.supercleaner.data.TaskListBoost;
import com.lubuteam.sellsourcecode.supercleaner.data.TaskOpenForceStop;
import com.lubuteam.sellsourcecode.supercleaner.dialog.DialogSelection;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.service.ForceStopAccessibility;
import com.lubuteam.sellsourcecode.supercleaner.widget.CpuScanView;
import com.lubuteam.sellsourcecode.supercleaner.widget.PowerScanView;
import com.lubuteam.sellsourcecode.supercleaner.widget.RocketScanView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneBoostActivity extends BaseActivity {
    /**
     * This activity used for function:
     * - Phone boost
     * - CPU Cooler
     * - power saver
     */

    @BindView(R.id.rocketScanView)
    RocketScanView mRocketScanView;
    @BindView(R.id.cpuScanView)
    CpuScanView mCpuScanView;
    @BindView(R.id.powerScanView)
    PowerScanView mPowerScanView;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.im_back_toolbar)
    ImageView imBack;
    @BindView(R.id.tv_num_appskill)
    TextView tvNumberAppKill;
//    @BindView(R.id.ll_phone_booster)
//    RelativeLayout llPhoneBooster;
    @BindView(R.id.rcv_app)
    RecyclerView rcvAppRunning;
    @BindView(R.id.cb_select_all)
    CheckBox cbSelectAll;
    @BindView(R.id.tv_content_header)
    TextView tvContentHeader;
    @BindView(R.id.tv_select_all)
    TextView tvSelectAll;
    @BindView(R.id.tv_boost)
    TextView tvBoost;
    @BindView(R.id.id_menu_toolbar)
    ImageView imMenuToolbar;

    private boolean anmationRunning = false;
    private List<TaskInfo> lstApp = new ArrayList<>();
    private AppSelectAdapter mAppSelectAdapter;
    private Config.FUNCTION mFunction;

    public static void startActivityWithData(Context mContext, Config.FUNCTION mFunction) {
        Intent mIntent = new Intent(mContext, PhoneBoostActivity.class);
        mIntent.putExtra(Config.DATA_OPEN_BOOST, mFunction);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_boost);
        ButterKnife.bind(this);
        mFunction = (Config.FUNCTION) getIntent().getSerializableExtra(Config.DATA_OPEN_BOOST);
        initView();
        initData();
    }

    private void initView() {
        imBack.setVisibility(View.VISIBLE);
        if (mFunction != null)
            tvToolbar.setText(getString(mFunction.title));
        switch (mFunction) {
            case PHONE_BOOST:
                tvContentHeader.setText(R.string.memory_kill_found);
                tvSelectAll.setText(R.string.running_app);
                tvBoost.setText(R.string.boost);
                mRocketScanView.setVisibility(View.VISIBLE);
                mRocketScanView.playAnimationStart();
                break;
            case CPU_COOLER:
                tvContentHeader.setText(R.string.further_optimize_cpu);
                tvSelectAll.setText(R.string.cpu_heating_app);
                tvBoost.setText(R.string.cool_down);
                mCpuScanView.setVisibility(View.VISIBLE);
                mCpuScanView.playAnimationStart();
                break;
            case POWER_SAVING:
                tvContentHeader.setText(R.string.secretly_consuming_battery);
                tvSelectAll.setText(R.string.battery_draining_app);
                tvBoost.setText(R.string.extend_battery_life);
                mPowerScanView.setVisibility(View.VISIBLE);
                mPowerScanView.playAnimationStart();
                break;
        }
    }

    private void initData() {
        mAppSelectAdapter = new AppSelectAdapter(this, AppSelectAdapter.TYPE_SELECT.CHECK_BOX, lstApp);
        rcvAppRunning.setAdapter(mAppSelectAdapter);
        getListAppRunning();
    }

    public void getListAppRunning() {
        anmationRunning = true;
        new TaskListBoost(new TaskListBoost.OnTaskListListener() {
            @Override
            public void OnResult(List<TaskInfo> arrList) {
                anmationRunning = false;
                if (arrList != null) {
                    Collections.sort(arrList, (o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
                    lstApp.clear();
                    lstApp.addAll(arrList);
                    setListStatusItem(true);
                    cbSelectAll.setChecked(true);
                    mAppSelectAdapter.notifyDataSetChanged();
                    tvNumberAppKill.setText(String.valueOf(arrList.size()));
//                    llPhoneBooster.setBackgroundResource(R.drawable.bg_boost_result_gradient);
                    setViewAffterScan();
                }
            }

            @Override
            public void onProgress(String appName) {
                mRocketScanView.setContent("<b>" + getString(R.string.scaning) + "</b>" + appName);
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setViewAffterScan() {
        imMenuToolbar.setVisibility(View.VISIBLE);
        switch (mFunction) {
            case PHONE_BOOST:
                mRocketScanView.stopAnimationStart();
                break;
            case CPU_COOLER:
                mCpuScanView.stopAnimationStart();
                break;
            case POWER_SAVING:
                mPowerScanView.stopAnimationStart();
                break;
        }
    }

    private void playAnimationDone() {
        switch (mFunction) {
            case PHONE_BOOST:
                YoYo.with(Techniques.FadeIn).duration(1000).playOn(mRocketScanView);
                mRocketScanView.playAnimationDone(() -> {
                    finishAnimationDone();
                });
                break;
            case CPU_COOLER:
                YoYo.with(Techniques.FadeIn).duration(1000).playOn(mCpuScanView);
                mCpuScanView.playAnimationDone(() -> {
                    finishAnimationDone();
                });
                break;
            case POWER_SAVING:
                YoYo.with(Techniques.FadeIn).duration(1000).playOn(mPowerScanView);
                List<TaskInfo> lstSelect = new ArrayList<>();
                for (TaskInfo mTaskInfo : lstApp) {
                    if (mTaskInfo.isChceked())
                        lstSelect.add(mTaskInfo);
                }
                mPowerScanView.playAnimationDone(lstSelect, 300, () -> {
                    finishAnimationDone();
                });
                break;
        }
    }

    private void finishAnimationDone() {
        anmationRunning = false;
        openScreenResult(mFunction);
        finish();
    }

    private void runkillApp() {
        anmationRunning = true;
        new KillAppProgress(this, lstApp).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        playAnimationDone();
    }

    private void setListStatusItem(boolean status) {
        for (TaskInfo mTaskInfo : lstApp) {
            mTaskInfo.setChceked(status);
        }
    }

    @Override
    public void onBackPressed() {
        if (!anmationRunning)
            super.onBackPressed();
    }

    @OnClick({R.id.tv_boost, R.id.cb_select_all, R.id.id_menu_toolbar})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.id_menu_toolbar:
                if (!anmationRunning) {
                    PopupMenu popupMenu = new PopupMenu(this, imMenuToolbar);
                    popupMenu.getMenuInflater().inflate(R.menu.phone_boost_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(item -> {
                        openIgnoreScreen();
                        return true;
                    });
                    popupMenu.show();
                }
                break;
            case R.id.cb_select_all:
                setListStatusItem(cbSelectAll.isChecked());
                mAppSelectAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_boost:
                if (checkEmptyItemSelect()) {
                    if (mFunction == Config.FUNCTION.POWER_SAVING) {
                        if (ForceStopAccessibility.getInstance() != null) {
                            validateDeepClean();
                        } else {
                            new DialogSelection.Builder()
                                    .setTitle(getString(R.string.deep_boost))
                                    .setContent(getString(R.string.content_permission_1))
                                    .setAction1(getString(R.string.deep_boost), dialog -> {
                                        dialog.dismiss();
                                        validateDeepClean();
                                    })
                                    .setAction2(getString(R.string.normal_boost), dialog -> {
                                        runkillApp();
                                        dialog.dismiss();
                                    })
                                    .build()
                                    .show(getSupportFragmentManager(), DialogSelection.class.getName());
                        }
                    } else {
                        runkillApp();
                    }
                }
                break;
        }
    }

    private void validateDeepClean() {
        try {
            checkdrawPermission(() -> {
                if (ForceStopAccessibility.getInstance() != null) {
                    startDeepClean();
                } else {
                    requestDetectHome();
                }
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startDeepClean() {
        List<TaskInfo> lstSelect = new ArrayList<>();
        for (TaskInfo mTaskInfo : lstApp) {
            if (mTaskInfo.isChceked())
                lstSelect.add(mTaskInfo);
        }
        new TaskOpenForceStop(this, lstSelect).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean checkEmptyItemSelect() {
        for (TaskInfo mTaskInfo : lstApp) {
            if (mTaskInfo.isChceked())
                return true;
        }
        Toast.makeText(this, getString(R.string.empty_item_select), Toast.LENGTH_LONG).show();
        return false;
    }

}
