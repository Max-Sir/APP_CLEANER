package com.lubuteam.sellsourcecode.supercleaner.screen.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ads.control.Rate;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.adapter.CustomFragmentPagerAdapter;
import com.lubuteam.sellsourcecode.supercleaner.listener.ObserverPartener.ObserverInterface;
import com.lubuteam.sellsourcecode.supercleaner.listener.ObserverPartener.ObserverUtils;
import com.lubuteam.sellsourcecode.supercleaner.listener.ObserverPartener.eventModel.EvbCheckLoadAds;
import com.lubuteam.sellsourcecode.supercleaner.listener.ObserverPartener.eventModel.EvbOpenFunc;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.ExitActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.main.home.FragmentHome;
import com.lubuteam.sellsourcecode.supercleaner.screen.main.personal.FragmentPersional;
import com.lubuteam.sellsourcecode.supercleaner.screen.main.tool.FragmentTool;
import com.lubuteam.sellsourcecode.supercleaner.service.NotificationUtil;
import com.lubuteam.sellsourcecode.supercleaner.service.ServiceManager;
import com.lubuteam.sellsourcecode.supercleaner.utils.AlarmUtils;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;
import com.lubuteam.sellsourcecode.supercleaner.widget.CustomViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements ObserverInterface {

    @BindView(R.id.bottom_control_view)
    BottomNavigationView mBottomNavigationView;
//    @BindView(R.id.tv_toolbar)
//    TextView tvTitleToolbar;
    @BindView(R.id.viewpager_home)
    CustomViewPager mViewPagerHome;

    private boolean doubleBackToExitPressedOnce = false;
    private CustomFragmentPagerAdapter mCustomFragmentPagerAdapter;
    private boolean loadAdsNative = true;
    private FragmentHome mFragmentHome;

    public boolean isLoadAdsNative() {
        return loadAdsNative;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ObserverUtils.getInstance().registerObserver(this);
        Intent mIntent = new Intent(this, ServiceManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, mIntent);
        } else {
            startService(mIntent);
        }
        initView();
        initData();
        initControl();

    }

    private void initView() {
        mCustomFragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        mFragmentHome = FragmentHome.getInstance();
        mCustomFragmentPagerAdapter.addFragment(mFragmentHome, getString(R.string.title_home));
        mCustomFragmentPagerAdapter.addFragment(FragmentTool.getInstance(), getString(R.string.title_tool));
        mCustomFragmentPagerAdapter.addFragment(FragmentPersional.getInstance(), getString(R.string.title_persional));
        mViewPagerHome.setPagingEnabled(false);
        mViewPagerHome.setOffscreenPageLimit(mCustomFragmentPagerAdapter.getCount());
        mViewPagerHome.setAdapter(mCustomFragmentPagerAdapter);

//        tvTitleToolbar.setTextSize(20);
    }

    private void initData() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
//                    tvTitleToolbar.setText(getString(R.string.title_home));
                    mViewPagerHome.setCurrentItem(0);
                    return true;
                case R.id.navigation_tools:
//                    tvTitleToolbar.setText(getString(R.string.title_tool));
                    mViewPagerHome.setCurrentItem(1);
                    return true;
                case R.id.navigation_personal:
//                    tvTitleToolbar.setText("");
                    mViewPagerHome.setCurrentItem(2);
                    return true;
            }
            return false;
        });
        mBottomNavigationView.setSelectedItemId(R.id.navigation_home);

    }

    private void initControl() {
        Config.FUNCTION mFunction = Config.getFunctionById(getIntent().getIntExtra(Config.DATA_OPEN_RESULT, 0));
        if (mFunction != null) {
            loadAdsNative = false;
            openScreenResult(mFunction);
        } else {
            Config.FUNCTION mFunctionService = Config.getFunctionById(getIntent().getIntExtra(Config.DATA_OPEN_FUNCTION, 0));
            if (mFunctionService != null) {
                for (Config.FUNCTION item : Config.LST_TOOL_CLEAN_BOOST) {
                    if (item == mFunctionService) {
                        mBottomNavigationView.setSelectedItemId(R.id.navigation_tools);
                        break;
                    }
                }
                for (Config.FUNCTION item : Config.LST_TOOL_SECURITY) {
                    if (item == mFunctionService) {
                        mBottomNavigationView.setSelectedItemId(R.id.navigation_tools);
                        break;
                    }
                }
                loadAdsNative = false;
                openScreenFunction(mFunctionService);
            } else {
                int idFunc = getIntent().getIntExtra(Config.ALARM_OPEN_FUNTION, 0);
                Config.FUNCTION mFunctionAlarm = Config.getFunctionById(idFunc);
                if (mFunctionAlarm != null) {
                    loadAdsNative = false;
                    openScreenFunction(mFunctionAlarm);
                    if (mFunctionAlarm == Config.FUNCTION.PHONE_BOOST) {
                        NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFICATTION_PHONE_BOOST);
                        PreferenceUtils.setTimeAlarmPhoneBoost(AlarmUtils.TIME_PHONE_BOOST_CLICK);
                        AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_BOOOST, AlarmUtils.TIME_PHONE_BOOST_CLICK);
                    } else if (mFunctionAlarm == Config.FUNCTION.CPU_COOLER) {
                        NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFICATTION_CPU_COOLER);
                        PreferenceUtils.setTimeAlarmPhoneBoost(AlarmUtils.TIME_CPU_COOLER_CLICK);
                        AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_CPU_COOLER, AlarmUtils.TIME_CPU_COOLER_CLICK);
                    } else if (mFunctionAlarm == Config.FUNCTION.POWER_SAVING) {
                        NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFICATTION_BATTERY_SAVE);
                        PreferenceUtils.setTimeAlarmPhoneBoost(AlarmUtils.TIME_BATTERY_SAVE_CLICK);
                        AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_BATTERY_SAVE, AlarmUtils.TIME_BATTERY_SAVE_CLICK);
                    } else if (mFunctionAlarm == Config.FUNCTION.JUNK_FILES) {
                        NotificationUtil.getInstance().cancelNotificationClean(NotificationUtil.ID_NOTIFICATTION_JUNK_FILE);
                    }
                } else {
                    boolean isResultDeepClean = getIntent().getBooleanExtra(Config.REUSLT_DEEP_CLEAN_DATA, false);
                    if (isResultDeepClean) {
                        mBottomNavigationView.setSelectedItemId(R.id.navigation_tools);
                        loadAdsNative = false;
                        openScreenResult(Config.FUNCTION.DEEP_CLEAN);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new Handler().postDelayed(() -> {
//            if (CleanMasterApp.getInstance().getTopActivity() instanceof MainActivity) {
//                if (!BackgroundManager.getInstance().init(this).isServiceRunning(ForceStopAccessibility.class)) {
//                    requestDetectHome();
//                }
//            }
//        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ObserverUtils.getInstance().removeObserver(this);
    }

    @Override
    public void notifyAction(Object action) {
        if (action instanceof EvbOpenFunc) {
            openScreenFunction(((EvbOpenFunc) action).mFunction);
        } else if (action instanceof EvbCheckLoadAds) {
            loadAdsNative = true;
            if (mFragmentHome != null)
                mFragmentHome.loadAds();
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPagerHome.getCurrentItem() != 0) {
            mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
            return;
        }
        Rate.Show(this, () -> ExitActivity.exitApplicationAndRemoveFromRecent(MainActivity.this));
    }
}
