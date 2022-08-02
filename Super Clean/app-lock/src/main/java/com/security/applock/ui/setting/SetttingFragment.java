package com.security.applock.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.control.funtion.UtilsApp;
import com.security.applock.App;
import com.security.applock.R;
import com.security.applock.databinding.FragmentSettingBinding;
import com.security.applock.dialog.DialogSelectItem;
import com.security.applock.ui.BaseFragment;
import com.security.applock.ui.password.PasswordActivity;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;
import com.security.applock.utils.SystemUtil;

public class SetttingFragment extends BaseFragment<FragmentSettingBinding> {
    private static final int REQUEST_SWITCH_TO_PIN_CODE = 511;
    private static final int REQUEST_SWITCH_TO_PATERN_CODE = 512;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initView() {
        new Handler().postDelayed(() -> {

//            boolean isHidePattern = PreferencesHelper.getBoolean(PreferencesHelper.HIDE_PATTERN, false);
//            binding.funcHidePattern.setSwChecked(isHidePattern);

            boolean isNewApplock = PreferencesHelper.getBoolean(PreferencesHelper.SETTING_NEW_APP_LOCK, false);
            binding.funcLockNewApp.setSwChecked(isNewApplock);

            boolean isFingerprint = PreferencesHelper.getBoolean(PreferencesHelper.FINGERPRINT_UNLOCK, false);
            binding.funcFingerprint.setSwChecked(isFingerprint);

            if (!SystemUtil.hasFingerprint(requireContext())) {
                binding.funcFingerprintSeparator.setVisibility(View.GONE);
                binding.funcFingerprint.setVisibility(View.GONE);
            }

            boolean isIntruderSelfie = PreferencesHelper.getBoolean(PreferencesHelper.INTRUDER_SELFIE, false);
            binding.funcIntruderSelfie.setSwChecked(isIntruderSelfie);

        }, 50);
        binding.funcSwitchPassword.setTitle(PreferencesHelper.isPatternCode()
                ? getResources().getString(R.string.switch_to_pin)
                : getResources().getString(R.string.switch_to_patern));

    }

    @Override
    protected void initControl() {
        binding.funcLimitTime.setItemClickListener(this::showDialogSetTimeLock);
        binding.funcChangePassword.setItemClickListener(() -> {
            changePassword();
        });
//        binding.funcHidePattern.setActionListener(isChecked -> {
//            PreferencesHelper.putBoolean(PreferencesHelper.HIDE_PATTERN, isChecked);
//        });
        binding.funcSwitchPassword.setItemClickListener(() -> {
            App.getInstace().setForceLockScreen(true);
            if (PreferencesHelper.isPatternCode()) {
                gotoCheckPatternCode();
            } else {
                gotoCheckPinCode();
            }
        });

        binding.funcLockNewApp.setActionListener(isChecked -> {
            PreferencesHelper.putBoolean(PreferencesHelper.SETTING_NEW_APP_LOCK, isChecked);
        });

        binding.funcSercurityQuestion.setItemClickListener(() -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Config.KeyBundle.KEY_CHANGE_QUESTION, Config.DataBundle.DATA_CHANGE_QUESTION);
            navigate(R.id.action_setting_to_question, bundle);
        });

        binding.funcAdvancedDetection.setItemClickListener(() -> {
            navigate(R.id.action_nav_setting_to_acvanced);
        });

        binding.funcFingerprint.setActionListener(isChecked -> {
            PreferencesHelper.putBoolean(PreferencesHelper.FINGERPRINT_UNLOCK, isChecked);
        });


//        binding.funcLanguage.setItemClickListener(() -> {
//            new DialogSelectLanguage.ExtendBuilder()
//                    .setTitle(getString(R.string.change_language))
//                    .onSetNegativeButton(getString(R.string.cancel), baseDialog -> baseDialog.dismiss())
//                    .onSetPositiveButton(getString(R.string.ok), (baseDialog, datas) -> {
//                        App.getInstace().setForceLockScreen(true);
//                        getActivity().finishAffinity();
//                        startActivity(getActivity().getIntent());
//                    })
//                    .build()
//                    .show(getChildFragmentManager(), DialogSound.class.getName());
//        });

        binding.funcRate.setItemClickListener(() -> UtilsApp.RateApp(getActivity()));

        binding.funcIntruderSelfie.setActionListener(isChecked -> {
            if (!isChecked) {
                PreferencesHelper.putBoolean(PreferencesHelper.INTRUDER_SELFIE, false);
                return;
            }
            binding.funcIntruderSelfie.setSwChecked(false);
            getBaseActivity().askPermissionStorage(() -> {
                getBaseActivity().askPermissionCamera(() -> {
                    PreferencesHelper.putBoolean(PreferencesHelper.INTRUDER_SELFIE, true);
                    binding.funcIntruderSelfie.setSwChecked(true);
                    return null;
                });
                return null;
            });
        });

        binding.funcIntruderSelfie.setItemClickListener(() -> {
            if (binding.funcIntruderSelfie.getSwChecked())
                navigate(R.id.action_nav_setting_to_nav_intruder);
        });

    }

    private void changePassword() {
        App.getInstace().setForceLockScreen(true);
        Intent intent = new Intent(getActivity(), PasswordActivity.class);
        if (PreferencesHelper.isPatternCode())
            intent.setAction(Config.ActionIntent.ACTION_CHANGE_PATTERN_CODE);
        else
            intent.setAction(Config.ActionIntent.ACTION_CHANGE_PIN_CODE);
        startActivity(intent);
    }

    private void gotoCheckPinCode() {
        Intent intent = new Intent(getActivity(), PasswordActivity.class);
        intent.setAction(Config.ActionIntent.ACTION_SWITCH_TO_PATTERN_CODE);
        startActivityForResult(intent, REQUEST_SWITCH_TO_PATERN_CODE);
    }

    private void gotoCheckPatternCode() {
        Intent intent = new Intent(getActivity(), PasswordActivity.class);
        intent.setAction(Config.ActionIntent.ACTION_SWITCH_TO_PIN_CODE);
        startActivityForResult(intent, REQUEST_SWITCH_TO_PIN_CODE);
    }

    private void showDialogSetTimeLock() {
        new DialogSelectItem.ExtendBuilder()
                .setLstData(Config.lstTimeLock)
                .setIdDefault(PreferencesHelper.getInt(PreferencesHelper.SETTING_VALUE_TIME_LOCK
                        , Config.DEFAULT_TIME_LOCK))
                .setTitle(getString(R.string.limited_lock_time))
                .onSetPositiveButton(getString(R.string.save), (baseDialog, datas) -> {
                    int idSelected = (int) datas.get(DialogSelectItem.ITEM_SAVE);
                    PreferencesHelper.putInt(PreferencesHelper.SETTING_VALUE_TIME_LOCK, idSelected);
                    baseDialog.dismiss();
                })
                .build()
                .show(getChildFragmentManager(), DialogSelectItem.class.getName());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SWITCH_TO_PATERN_CODE:
                case REQUEST_SWITCH_TO_PIN_CODE:
                    binding.funcSwitchPassword.setTitle(PreferencesHelper.isPatternCode()
                            ? getResources().getString(R.string.switch_to_pin)
                            : getResources().getString(R.string.switch_to_patern));
                    break;
            }
        }
    }

    @Override
    protected FragmentSettingBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSettingBinding.inflate(inflater, container, false);
    }

    @Override
    protected int getTitleFragment() {
        return R.string.setting;
    }
}
