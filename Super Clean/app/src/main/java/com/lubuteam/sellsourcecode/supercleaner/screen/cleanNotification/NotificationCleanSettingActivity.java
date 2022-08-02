package com.lubuteam.sellsourcecode.supercleaner.screen.cleanNotification;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsourcecode.supercleaner.AppClean;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.adapter.AppSelectAdapter;
import com.lubuteam.sellsourcecode.supercleaner.data.TaskGetListApp;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.service.NotificationListener;
import com.lubuteam.sellsourcecode.supercleaner.service.NotificationUtil;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationCleanSettingActivity extends BaseActivity {

    @BindView(R.id.im_back_toolbar)
    ImageView imBackToolbar;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.rcv_social_network)
    RecyclerView rcvNetwork;
    @BindView(R.id.rcv_third_party)
    RecyclerView rcvThirdParty;
    @BindView(R.id.ll_network_app)
    View llNetworkApp;
    @BindView(R.id.ll_third_app)
    View llThirdApp;
    @BindView(R.id.google_progress)
    GoogleProgressBar mGoogleProgressBar;
    @BindView(R.id.sw_network_all)
    SwitchCompat swNetworkAll;
    @BindView(R.id.sw_third_app_all)
    SwitchCompat swThirdAppAll;
    @BindView(R.id.id_menu_toolbar)
    ImageView imActionToolbar;
    @BindView(R.id.sw_clean_notification)
    SwitchCompat swCleanNotification;
    @BindView(R.id.ll_list_app)
    View llListApp;
    @BindView(R.id.ll_list_app2)
    View llListApp2;

    private AppSelectAdapter mAppSelectAdapterNetwork;
    private AppSelectAdapter mAppSelectAdapterThirdParty;
    private List<TaskInfo> lstNetWork = new ArrayList<>();
    private List<TaskInfo> lstThirdParty = new ArrayList<>();
    private List<String> lstAppSave = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_notification_setting);
        ButterKnife.bind(this);
        PreferenceUtils.setFirstUsedFunction(Config.FUNCTION.NOTIFICATION_MANAGER);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        imBackToolbar.setVisibility(View.VISIBLE);
        tvToolbar.setText(getString(R.string.notification_manager));
        imActionToolbar.setVisibility(View.VISIBLE);
        imActionToolbar.setImageResource(R.drawable.ic_check_white_24dp);
        /**/
        Drawable drawable = new ChromeFloatingCirclesDrawable.Builder(this)
                .colors(Toolbox.getProgressDrawableColors(this))
                .build();
        Rect bounds = mGoogleProgressBar.getIndeterminateDrawable().getBounds();
        mGoogleProgressBar.setIndeterminateDrawable(drawable);
        mGoogleProgressBar.getIndeterminateDrawable().setBounds(bounds);
    }

    private void initData() {
        lstAppSave = PreferenceUtils.getListAppCleanNotifi();
        mAppSelectAdapterNetwork = new AppSelectAdapter(this, AppSelectAdapter.TYPE_SELECT.SWITCH, lstNetWork);
        rcvNetwork.setAdapter(mAppSelectAdapterNetwork);

        mAppSelectAdapterThirdParty = new AppSelectAdapter(this, AppSelectAdapter.TYPE_SELECT.SWITCH, lstThirdParty);
        rcvThirdParty.setAdapter(mAppSelectAdapterThirdParty);

        new TaskGetListApp(this, lstApp -> {
            lstNetWork.clear();
            lstThirdParty.clear();
            if (!lstApp.isEmpty()) {
                for (TaskInfo mTaskInfo : lstApp) {
                    if (isAppNetwork(mTaskInfo.getPackageName()))
                        lstNetWork.add(mTaskInfo);
                    else
                        lstThirdParty.add(mTaskInfo);
                }
                if (!lstNetWork.isEmpty())
                    llNetworkApp.setVisibility(View.VISIBLE);
                if (!lstThirdParty.isEmpty())
                    llThirdApp.setVisibility(View.VISIBLE);
                fillterAppSelect();
                setStateListApp(PreferenceUtils.isTurnOnCleanNotification());
                mAppSelectAdapterNetwork.notifyDataSetChanged();
                mAppSelectAdapterThirdParty.notifyDataSetChanged();
            }
            mGoogleProgressBar.setVisibility(View.GONE);
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        swCleanNotification.setChecked(PreferenceUtils.isTurnOnCleanNotification());
    }

    private void initListener() {
        mAppSelectAdapterNetwork.setmItemClickListener(position -> {
            boolean state = lstNetWork.get(position).isChceked();
            if (!state) {
                swNetworkAll.setChecked(false);
            } else {
                for (TaskInfo mTaskInfo : lstNetWork) {
                    if (!mTaskInfo.isChceked())
                        return;
                }
                swNetworkAll.setChecked(true);
            }
        });

        mAppSelectAdapterThirdParty.setmItemClickListener(position -> {
            boolean state = lstThirdParty.get(position).isChceked();
            if (!state) {
                swThirdAppAll.setChecked(false);
            } else {
                for (TaskInfo mTaskInfo : lstThirdParty) {
                    if (!mTaskInfo.isChceked())
                        return;
                }
                swThirdAppAll.setChecked(true);
            }
        });
    }

    private void setStateListApp(boolean state) {
        llListApp.setEnabled(state);
        llListApp.setAlpha(state ? 1.0f : 0.6f);
        llListApp2.setEnabled(state);
        llListApp2.setAlpha(state ? 1.0f : 0.6f);
        for (TaskInfo mTaskInfo : lstNetWork) {
            mTaskInfo.setClickEnable(state);
        }
        for (TaskInfo mTaskInfo : lstThirdParty) {
            mTaskInfo.setClickEnable(state);
        }
    }

    private void fillterAppSelect() {
        int coutNetWork = 0;
        int coutThirdApp = 0;
        for (String pkgNameSave : lstAppSave) {
            boolean check = false;
            for (TaskInfo mTaskInfo : lstNetWork) {
                if (mTaskInfo.getPackageName().equalsIgnoreCase(pkgNameSave)) {
                    mTaskInfo.setChceked(true);
                    check = true;
                    coutNetWork++;
                    break;
                }
            }
            if (!check) {
                for (TaskInfo mTaskInfo : lstThirdParty) {
                    if (mTaskInfo.getPackageName().equalsIgnoreCase(pkgNameSave)) {
                        mTaskInfo.setChceked(true);
                        coutThirdApp++;
                        break;
                    }
                }
            }
        }
        swNetworkAll.setChecked(coutNetWork == lstNetWork.size());
        swThirdAppAll.setChecked(coutThirdApp == lstThirdParty.size());
    }

    private boolean isAppNetwork(String pkgName) {
        for (String pkgNetwork : Config.LIST_APP_NETWORK) {
            if (pkgNetwork.equalsIgnoreCase(pkgName))
                return true;
        }
        return false;
    }

    @OnClick({R.id.id_menu_toolbar, R.id.sw_network_all, R.id.sw_third_app_all, R.id.sw_clean_notification})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.id_menu_toolbar:
                PreferenceUtils.setCleanNotification(swCleanNotification.isChecked());
                lstAppSave.clear();
                for (TaskInfo mTaskInfo : lstNetWork) {
                    if (mTaskInfo.isChceked())
                        lstAppSave.add(mTaskInfo.getPackageName());
                }
                for (TaskInfo mTaskInfo : lstThirdParty) {
                    if (mTaskInfo.isChceked())
                        lstAppSave.add(mTaskInfo.getPackageName());
                }
                PreferenceUtils.setListAppCleanNotifi(lstAppSave);
                if (swCleanNotification.isChecked() && NotificationListener.getInstance() != null) {
                    NotificationListener.getInstance().loadCurrentNotifi();
                } else {
                    NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFI_CLEAN);
                }
                onBackPressed();
                break;
            case R.id.sw_network_all:
                if (swCleanNotification.isChecked()) {
                    for (TaskInfo mTaskInfo : lstNetWork) {
                        mTaskInfo.setChceked(swNetworkAll.isChecked());
                    }
                    mAppSelectAdapterNetwork.notifyDataSetChanged();
                } else {
                    swNetworkAll.setChecked(!swNetworkAll.isChecked());
                }
                break;
            case R.id.sw_third_app_all:
                if (swCleanNotification.isChecked()) {
                    for (TaskInfo mTaskInfo : lstThirdParty) {
                        mTaskInfo.setChceked(swThirdAppAll.isChecked());
                    }
                    mAppSelectAdapterThirdParty.notifyDataSetChanged();
                } else {
                    swThirdAppAll.setChecked(!swThirdAppAll.isChecked());
                }
                break;
            case R.id.sw_clean_notification:
                setStateListApp(swCleanNotification.isChecked());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!PreferenceUtils.isTurnOnCleanNotification()) {
            AppClean.getInstance().clearAllActivityUnlessMain();
        } else {
            super.onBackPressed();
        }
    }
}
