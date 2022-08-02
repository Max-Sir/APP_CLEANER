package com.lubuteam.sellsourcecode.supercleaner.screen.smartCharger;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmartChargerActivity extends BaseActivity {

    @BindView(R.id.ll_splash_charger)
    View llSplashCharger;
    @BindView(R.id.ll_content)
    View llContent;
    @BindView(R.id.im_back_toolbar)
    ImageView imBack;
    @BindView(R.id.sw_onoff)
    SwitchCompat swOnOff;
    @BindView(R.id.sw_wifi)
    SwitchCompat swWifi;
    @BindView(R.id.sw_brightness)
    SwitchCompat swBrightness;
    @BindView(R.id.sw_bluetooth)
    SwitchCompat swBluetooth;
    @BindView(R.id.sw_synchronized)
    SwitchCompat swSynchronized;
    @BindView(R.id.sw_charging_finish)
    SwitchCompat swChargingFinish;
    @BindView(R.id.tv_status_wifi)
    TextView tvStatusWifi;
    @BindView(R.id.tv_status_brightness)
    TextView tvStatusBrightness;
    @BindView(R.id.tv_status_bluetooth)
    TextView tvStatusBluetooth;
    @BindView(R.id.tv_status_sync)
    TextView tvStatusSync;
    @BindView(R.id.ll_setting_charger)
    View llSettingsCharger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_charger);
        ButterKnife.bind(this);
        initView();
        initData();
        initControl();
    }

    private void initView() {
        imBack.setVisibility(View.VISIBLE);
        if (PreferenceUtils.isFirstUsedFunction(Config.FUNCTION.SMART_CHARGE)) {
            llSplashCharger.setVisibility(View.VISIBLE);
        } else {
            llContent.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        swChargingFinish.setChecked(PreferenceUtils.isNotifiBatteryFull());
        swOnOff.setChecked(PreferenceUtils.isOnSmartCharger());
        swWifi.setChecked(PreferenceUtils.getValueRecharger(PreferenceUtils.RECHARGE_WIFI));
        swBrightness.setChecked(PreferenceUtils.getValueRecharger(PreferenceUtils.RECHARGE_BRIGHTNESS));
        swBluetooth.setChecked(PreferenceUtils.getValueRecharger(PreferenceUtils.RECHARGE_BLUETOOTH));
        swSynchronized.setChecked(PreferenceUtils.getValueRecharger(PreferenceUtils.RECHARGE_SYNC));
        if (PreferenceUtils.isOnSmartCharger()) {
            llSettingsCharger.setEnabled(true);
            llSettingsCharger.setAlpha(1.0f);
            swWifi.setEnabled(true);
            swBrightness.setEnabled(true);
            swBluetooth.setEnabled(true);
            swSynchronized.setEnabled(true);
        }
    }

    private void initControl() {
        swChargingFinish.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceUtils.setNotifiBatteryFull(isChecked));

        setTextStatusColor(tvStatusWifi, swWifi.isChecked());
        setTextStatusColor(tvStatusBrightness, swBrightness.isChecked());
        setTextStatusColor(tvStatusBluetooth, swBluetooth.isChecked());
        setTextStatusColor(tvStatusSync, swSynchronized.isChecked());

        swWifi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvStatusWifi.setText(isChecked ? getString(R.string.on) : getString(R.string.off));
            setTextStatusColor(tvStatusWifi, isChecked);
            PreferenceUtils.setValueRecharger(PreferenceUtils.RECHARGE_WIFI, isChecked);
        });

        swBrightness.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setTextStatusColor(tvStatusBrightness, isChecked);
            PreferenceUtils.setValueRecharger(PreferenceUtils.RECHARGE_BRIGHTNESS, isChecked);
        });

        swBluetooth.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvStatusBluetooth.setText(isChecked ? getString(R.string.on) : getString(R.string.off));
            setTextStatusColor(tvStatusBluetooth, isChecked);
            PreferenceUtils.setValueRecharger(PreferenceUtils.RECHARGE_BLUETOOTH, isChecked);
        });

        swSynchronized.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setTextStatusColor(tvStatusSync, isChecked);
            PreferenceUtils.setValueRecharger(PreferenceUtils.RECHARGE_SYNC, isChecked);
        });

        swOnOff.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PreferenceUtils.setSmartCharger(isChecked);
            swWifi.setChecked(isChecked);
            swBrightness.setChecked(isChecked);
            swWifi.setEnabled(isChecked);
            swBrightness.setEnabled(isChecked);
            swSynchronized.setEnabled(isChecked);
            swBluetooth.setEnabled(isChecked);
            PreferenceUtils.setValueRecharger(PreferenceUtils.RECHARGE_WIFI, isChecked);
            PreferenceUtils.setValueRecharger(PreferenceUtils.RECHARGE_BRIGHTNESS, isChecked);
            if (isChecked) {
                llSettingsCharger.setEnabled(true);
                llSettingsCharger.setAlpha(1.0f);
            } else {
                swSynchronized.setChecked(false);
                swBluetooth.setChecked(false);
                PreferenceUtils.setValueRecharger(PreferenceUtils.RECHARGE_BLUETOOTH, false);
                PreferenceUtils.setValueRecharger(PreferenceUtils.RECHARGE_SYNC, false);
                llSettingsCharger.setEnabled(false);
                llSettingsCharger.setAlpha(0.6f);
            }
        });
    }

    public void setTextStatusColor(TextView textView, boolean status) {
        textView.setTextColor(status ? getResources().getColor(R.color.color_6ad390) : getResources().getColor(R.color.color_a8a8a8));
    }

    @OnClick({R.id.ic_close, R.id.tv_turn_on, R.id.id_menu_toolbar})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.ic_close:
                finish();
                break;
            case R.id.tv_turn_on:
                YoYo.with(Techniques.FadeOutUp).duration(1000).onEnd(animator -> llSplashCharger.setVisibility(View.GONE)).playOn(llSplashCharger);
                llContent.setVisibility(View.VISIBLE);
                PreferenceUtils.setFirstUsedFunction(Config.FUNCTION.SMART_CHARGE);
                swOnOff.setChecked(true);
                swChargingFinish.setChecked(true);
                break;
            case R.id.id_menu_toolbar:
                openScreenFunction(Config.FUNCTION.SMART_CHARGE);
                finish();
                break;
        }
    }
}
