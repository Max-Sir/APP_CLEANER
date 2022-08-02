package com.lubuteam.sellsourcecode.supercleaner.screen.setting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.dialog.DialogSelectItem2;
import com.lubuteam.sellsourcecode.supercleaner.dialog.SelectModel2;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.notDissturb.NotDissturbActivity;
import com.lubuteam.sellsourcecode.supercleaner.service.ServiceManager;
import com.lubuteam.sellsourcecode.supercleaner.utils.AlarmUtils;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends BaseActivity {

    @BindView(R.id.im_back_toolbar)
    ImageView imBack;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.sw_uninstall_scan)
    SwitchCompat swUninstall;
    @BindView(R.id.sw_install_scan)
    SwitchCompat swInstall;
    @BindView(R.id.sw_phone_boost)
    SwitchCompat swPhoneBoost;
    @BindView(R.id.sw_cpu_cooler)
    SwitchCompat swCpuCooler;
    @BindView(R.id.sw_battery_save)
    SwitchCompat swBatterySave;
    @BindView(R.id.sw_protection_real_time)
    SwitchCompat swProtectRealTime;
    @BindView(R.id.sw_notificaiton_toggle)
    SwitchCompat swNotificationToggle;
    @BindView(R.id.tv_time_junk_remind)
    TextView tvTimeRemind;
    @BindView(R.id.tv_time_dnd)
    TextView tvTimeDnd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initView();
        initData();

    }

    private void initView() {
        imBack.setVisibility(View.VISIBLE);
//        imBack.setColorFilter(getResources().getColor(R.color.color_222222), PorterDuff.Mode.SRC_IN);
        tvToolbar.setText(getString(R.string.setting));
//        tvToolbar.setTextColor(getResources().getColor(R.color.color_222222));
        switch (PreferenceUtils.getTimeRemindJunkFile()) {
            case 0:
                tvTimeRemind.setText(getString(R.string.never_reminder));
                break;
            case 1:
                tvTimeRemind.setText(getString(R.string.every_day));
                break;
            case 3:
                tvTimeRemind.setText(getString(R.string.every_3days));
                break;
            case 7:
                tvTimeRemind.setText(getString(R.string.every_7days));
                break;
        }
    }

    public void initData() {
        swUninstall.setChecked(PreferenceUtils.isScanUninstaillApk());
        swInstall.setChecked(PreferenceUtils.isScanInstaillApk());
        swProtectRealTime.setChecked(PreferenceUtils.isProtectionRealTime());
        swNotificationToggle.setChecked(PreferenceUtils.isShowHideNotificationManager());
        /**/
        swPhoneBoost.setChecked(PreferenceUtils.getTimeAlarmPhoneBoost() != 0);
        swCpuCooler.setChecked(PreferenceUtils.getTimeAlarmCpuCooler() != 0);
        swBatterySave.setChecked(PreferenceUtils.getTimeAlarmBatterySave() != 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        tvTimeDnd.setText(Toolbox.intTimeOff(PreferenceUtils.getDNDStart()) + " - " + Toolbox.intTimeOn(PreferenceUtils.getDNDEnd()));
    }

    @OnClick({R.id.ll_create_shortcut, R.id.ll_app_protected, R.id.ll_ignore_list, R.id.sw_uninstall_scan, R.id.sw_install_scan
            , R.id.sw_phone_boost, R.id.sw_cpu_cooler, R.id.sw_battery_save, R.id.sw_protection_real_time, R.id.sw_notificaiton_toggle
            , R.id.ll_junk_reminder, R.id.ll_dissturb})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.ll_create_shortcut:
                Toolbox.creatShorCutNormal(this);
                break;
            case R.id.ll_app_protected:
                openIgnoreScreen();
                break;
            case R.id.ll_ignore_list:
                openWhileListVirusSceen();
                break;
            case R.id.sw_uninstall_scan:
                if (PreferenceUtils.isScanUninstaillApk()) {
                    PreferenceUtils.setScanUninstaillApk(false);
                } else {
                    swUninstall.setChecked(false);
                    try {
                        askPermissionStorage(() -> {
                            PreferenceUtils.setScanUninstaillApk(true);
                            swUninstall.setChecked(true);
                            return null;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.sw_install_scan:
                if (PreferenceUtils.isScanInstaillApk()) {
                    PreferenceUtils.setScaninstaillApk(false);
                } else {
                    swInstall.setChecked(false);
                    try {
                        askPermissionStorage(() -> {
                            PreferenceUtils.setScaninstaillApk(true);
                            swInstall.setChecked(true);
                            return null;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.sw_protection_real_time:
                if (PreferenceUtils.isProtectionRealTime()) {
                    PreferenceUtils.setProtectionRealTime(false);
                } else {
                    swProtectRealTime.setChecked(false);
                    try {
                        checkdrawPermission(() -> {
                            askPermissionStorage(() -> {
                                swProtectRealTime.setChecked(true);
                                PreferenceUtils.setProtectionRealTime(true);
                                return null;
                            });
                            return null;
                        });
                    } catch (Exception e) {

                    }
                }
                break;
            case R.id.sw_notificaiton_toggle:
                if (ServiceManager.getInstance() != null) {
                    if (!PreferenceUtils.isShowHideNotificationManager()) {
                        ServiceManager.getInstance().setRestartService(false);
                        ServiceManager.getInstance().onDestroy();
                        Intent mIntent = new Intent(this, ServiceManager.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            ContextCompat.startForegroundService(this, mIntent);
                        } else {
                            startService(new Intent(this, ServiceManager.class));
                        }
                        PreferenceUtils.setShowHideNotificationManager(true);
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.note))
                                .setMessage(getString(R.string.note_turn_off_notifi, getString(R.string.app_name)))
                                .setPositiveButton(getString(R.string.cancel), (dialog, which) -> {
                                    dialog.dismiss();
                                    swNotificationToggle.setChecked(true);
                                })
                                .setNegativeButton(getString(R.string.turn_off), (dialog, which) -> {
                                    ServiceManager.getInstance().deleteViewNotifi();
                                    PreferenceUtils.setShowHideNotificationManager(false);
                                })
                                .show();
                    }
                }
                break;
            case R.id.sw_phone_boost:
                if (swPhoneBoost.isChecked()) {
                    long time = new Random().nextInt(30) * 60 * 1000;
                    PreferenceUtils.setTimeAlarmPhoneBoost(time);
                    AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_BOOOST, time);
                } else {
                    PreferenceUtils.setTimeAlarmPhoneBoost(0);
                    AlarmUtils.cancel(this, AlarmUtils.ALARM_PHONE_BOOOST);
                }
                break;
            case R.id.sw_cpu_cooler:
                if (swCpuCooler.isChecked()) {
                    long time = new Random().nextInt(30) * 60 * 1000;
                    PreferenceUtils.setTimeAlarmCpuCooler(time);
                    AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_CPU_COOLER, time);
                } else {
                    PreferenceUtils.setTimeAlarmCpuCooler(0);
                    AlarmUtils.cancel(this, AlarmUtils.ALARM_PHONE_CPU_COOLER);
                }
                break;
            case R.id.sw_battery_save:
                if (swBatterySave.isChecked()) {
                    long time = new Random().nextInt(30) * 60 * 1000;
                    PreferenceUtils.setTimeAlarmBatterySave(time);
                    AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_BATTERY_SAVE, time);
                } else {
                    PreferenceUtils.setTimeAlarmBatterySave(0);
                    AlarmUtils.cancel(this, AlarmUtils.ALARM_PHONE_BATTERY_SAVE);
                }
                break;
            case R.id.ll_junk_reminder:
                selectTimeJunkFile();
                break;
            case R.id.ll_dissturb:
                startActivity(new Intent(this, NotDissturbActivity.class));
                break;
        }
    }


    public void selectTimeJunkFile() {

        SelectModel2[] lstJunkReminderFrequency = new SelectModel2[]{
                new SelectModel2(0, getString(R.string.every_day)),
                new SelectModel2(1, getString(R.string.every_3days)),
                new SelectModel2(2, getString(R.string.every_7days)),
                new SelectModel2(3, getString(R.string.never_reminder)),
        };

        new DialogSelectItem2.ExtendBuilder2()
                .setLstData(lstJunkReminderFrequency)
                .setItemClickListener(position -> {
                    String strName = lstJunkReminderFrequency[position].getTitle();
                    tvTimeRemind.setText(strName);
                    if (position == 0) {
                        PreferenceUtils.setTimeRemindJunkFile(1);
                    } else if (position == 1) {
                        PreferenceUtils.setTimeRemindJunkFile(3);
                    } else if (position == 2) {
                        PreferenceUtils.setTimeRemindJunkFile(7);
                    } else if (position == 3) {
                        PreferenceUtils.setTimeRemindJunkFile(0);
                    }
                    if (PreferenceUtils.getTimeRemindJunkFile() != 0) {
                        AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_JUNK_FILE, Toolbox.getTimeAlarmJunkFile(true));
                    } else {
                        AlarmUtils.cancel(this, AlarmUtils.ALARM_PHONE_JUNK_FILE);
                    }
                })
                .setTitle(getString(R.string.junk_reminder_frequency))
                .build()
                .show(getSupportFragmentManager(), DialogSelectItem2.class.getName());


//        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
//        builderSingle.setTitle(getString(R.string.junk_reminder_frequency));
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
//        arrayAdapter.add(getString(R.string.every_day));
//        arrayAdapter.add(getString(R.string.every_3days));
//        arrayAdapter.add(getString(R.string.every_7days));
//        arrayAdapter.add(getString(R.string.never_reminder));
//
//        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
//            String strName = arrayAdapter.getItem(which);
//            tvTimeRemind.setText(strName);
//            if (which == 0) {
//                PreferenceUtils.setTimeRemindJunkFile(1);
//            } else if (which == 1) {
//                PreferenceUtils.setTimeRemindJunkFile(3);
//            } else if (which == 2) {
//                PreferenceUtils.setTimeRemindJunkFile(7);
//            } else if (which == 3) {
//                PreferenceUtils.setTimeRemindJunkFile(0);
//            }
//            if (PreferenceUtils.getTimeRemindJunkFile() != 0) {
//                AlarmUtils.setAlarm(this, AlarmUtils.ALARM_PHONE_JUNK_FILE, Toolbox.getTimeAlarmJunkFile(true));
//            } else {
//                AlarmUtils.cancel(this, AlarmUtils.ALARM_PHONE_JUNK_FILE);
//            }
//        });
//        builderSingle.show();
    }
}
