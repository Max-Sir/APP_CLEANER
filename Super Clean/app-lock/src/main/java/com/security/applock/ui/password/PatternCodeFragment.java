package com.security.applock.ui.password;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.security.applock.App;
import com.security.applock.R;
import com.security.applock.databinding.FragmentPatternCodeBinding;
import com.security.applock.databinding.LayoutHeaderLockviewBinding;
import com.security.applock.dialog.DialogSetUpPassword;
import com.security.applock.service.AntiTheftService;
import com.security.applock.service.BackgroundManager;
import com.security.applock.ui.BaseFragment;
import com.security.applock.ui.forgotpassword.ForgotPasswordActivity;
import com.security.applock.utils.Camera2Controller;
import com.security.applock.utils.Config;
import com.security.applock.utils.FingerprintManager;
import com.security.applock.utils.PreferencesHelper;
import com.security.applock.utils.SystemUtil;
import com.security.applock.utils.Toolbox;
import com.security.applock.widget.PatternLockView;

public class PatternCodeFragment extends BaseFragment<FragmentPatternCodeBinding> {
    private Boolean isConfirmCode = false;
    private String codeConfirm = "";
    private String action = "";
    private Camera2Controller camera2Controller;
    private int countEntries = 0;
    private LayoutHeaderLockviewBinding headerLockviewBinding;
    private FingerprintManager fingerprintManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initView() {
        binding.tvForgotPassword.setPaintFlags(binding.tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        headerLockviewBinding = LayoutHeaderLockviewBinding.bind(binding.llHeaderLockview.getRoot());
        camera2Controller = new Camera2Controller(binding.textureview, requireActivity());
        boolean isHidePattern = PreferencesHelper.getBoolean(PreferencesHelper.HIDE_PATTERN, false);
        if (isHidePattern) binding.patternCode.hidePattern();
        action = requireActivity().getIntent().getAction() != null ? requireActivity().getIntent().getAction() : "";
        switch (action) {
            case Config.ActionIntent.ACTION_CHANGE_PATTERN_CODE:
                headerLockviewBinding.tvMessage.setText(getResources().getString(R.string.enter_pattern_code));
                break;
            case Config.ActionIntent.ACTION_SET_UP_PATTERN_CODE:
            case Config.ActionIntent.ACTION_SET_UP_PATTERN_CODE_WHEN_CHANGE:
                if (TextUtils.isEmpty(PreferencesHelper.getPatternCode()))
                    showDialogSetUpPassword();
                headerLockviewBinding.tvMessage.setText(getResources().getString(R.string.enter_new_pattern_code));
                break;
            case Config.ActionIntent.ACTION_CHECK_PASSWORD_FROM_SERVICE:
                String packageName = requireActivity().getIntent().getStringExtra(Config.KeyBundle.KEY_PACKAGE_NAME);
                try {
                    headerLockviewBinding.imvIcon.setImageDrawable(Toolbox.getdIconApplication(requireContext(), packageName));
                    headerLockviewBinding.tvMessage.setText(Toolbox.getdNameApplication(requireContext(), packageName));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                initFingerprint();
                break;
            case Config.ActionIntent.ACTION_CHECK_PASSWORD_ANTI_THEFT:
            case Config.ActionIntent.ACTION_CHECK_PATTERN_CODE:
                initFingerprint();
            case Config.ActionIntent.ACTION_SWITCH_TO_PIN_CODE:
            default:
                headerLockviewBinding.tvMessage.setText(getResources().getString(R.string.enter_pattern_code));
                break;
        }
    }

    private void initFingerprint() {

        if (!SystemUtil.hasFingerprint(requireContext()) || !PreferencesHelper.getBoolean(PreferencesHelper.FINGERPRINT_UNLOCK, false))
            return;

        fingerprintManager = new FingerprintManager(getActivity(), new FingerprintManager.FingerListener() {
            @Override
            public void onSuccessListener() {
                checkPatternCodeAntiTheftSuccess();
            }

            @Override
            public void onFailsListener() {

            }
        });
        fingerprintManager.initFinger();
    }

    private void showDialogSetUpPassword() {
        new DialogSetUpPassword.ExtendBuilder()
                .setTitle(getString(R.string.content_set_up_password))
                .onSetPositiveButton(getString(R.string.ok), (baseDialog, datas) -> baseDialog.dismiss())
                .build()
                .show(getChildFragmentManager(), DialogSetUpPassword.class.getName());
    }

    @Override
    protected void initControl() {
        binding.patternCode.setFinishInterruptable(false);
        binding.patternCode.setCallBack(password -> {
            switch (action) {
                case Config.ActionIntent.ACTION_SET_UP_PATTERN_CODE:
                    return checkSetUpPatternCode(password.string);
                case Config.ActionIntent.ACTION_SET_UP_PATTERN_CODE_WHEN_CHANGE:
                    return checkChangePatternCode(password.string);
                case Config.ActionIntent.ACTION_CHANGE_PATTERN_CODE:
                    return checkPatternCodeWhenChange(password.string);
                case Config.ActionIntent.ACTION_CHECK_PASSWORD_ANTI_THEFT:
                    return checkPatternCodeAntiTheft(password.string);
                case Config.ActionIntent.ACTION_SWITCH_TO_PIN_CODE:
                    // action switch pin code -> pattern code
                    return checkPatternCodeSwitch(password.string);
                case Config.ActionIntent.ACTION_SWITCH_TO_PATTERN_CODE:
                    // action switch pattern code -> pin code
                    return checkSetUpSwitchPatternCode(password.string);
                case Config.ActionIntent.ACTION_CHECK_PASSWORD_FROM_SERVICE:
                    return checkPatternCodeUnLockApp(password.string);
                default:
                    return checkPatternCode(password.string);
            }
        });

        binding.tvForgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
        });
    }

    private int checkPatternCodeUnLockApp(String code) {
        if (code.equals(PreferencesHelper.getPatternCode())) {
            return checkPatternCodeUnLockAppSuccess();
        } else {
            return checkPatternCodeFailed(code);
        }
    }

    private int checkPatternCodeUnLockAppSuccess() {
        App.getInstace().setForceLockScreen(true);
        requireActivity().finishAffinity();
        return PatternLockView.CODE_PASSWORD_CORRECT;
    }

    private int checkSetUpSwitchPatternCode(String code) {
        if (!isConfirmCode) {
            confirmCode(code);
        } else {
            if (code.equals(codeConfirm)) {
                setUpSwitchPatternCodeSuccess(code);
                return PatternLockView.CODE_PASSWORD_CORRECT;
            } else {
                setUpCodeFailed();
                return PatternLockView.CODE_PASSWORD_ERROR;
            }
        }
        return 0;
    }

    private void setUpSwitchPatternCodeSuccess(String patternCode) {
        PreferencesHelper.setPatternCode(patternCode);
        toast(getResources().getString(R.string.message_confirm_pattern_code_success));
        requireActivity().setResult(Activity.RESULT_OK);
        requireActivity().finish();
    }

    private int checkPatternCodeSwitch(String code) {
        if (code.equals(PreferencesHelper.getPatternCode())) {
            return checkPatternCodeSwitchSuccess();
        } else {
            return checkPatternCodeFailed(code);
        }
    }

    private int checkPatternCode(String code) {
        if (code.equals(PreferencesHelper.getPatternCode())) {
            return checkPatternCodeSuccess();
        } else {
            return checkPatternCodeFailed(code);
        }
    }

    private int checkPatternCodeWhenChange(String code) {
        if (code.equals(PreferencesHelper.getPatternCode())) {
            return checkPatternCodeSuccessWhenChange();
        } else {
            return checkPatternCodeFailed(code);
        }
    }

    private int checkPatternCodeSuccessWhenChange() {
        requireActivity().getIntent().setAction(Config.ActionIntent.ACTION_SET_UP_PATTERN_CODE_WHEN_CHANGE);
        initView();
        return PatternLockView.CODE_PASSWORD_CORRECT;
    }

    private int checkPatternCodeAntiTheft(String code) {
        if (code.equals(PreferencesHelper.getPatternCode())) {
            return checkPatternCodeAntiTheftSuccess();
        } else {
            return checkPatternCodeFailed(code);
        }
    }

    private int checkChangePatternCode(String code) {
        if (!isConfirmCode) {
            confirmCode(code);
        } else {
            if (code.equals(codeConfirm)) {
                changeCodeSuccess(code);
                return PatternLockView.CODE_PASSWORD_CORRECT;
            } else {
                changeCodeFailed();
                return PatternLockView.CODE_PASSWORD_ERROR;
            }
        }
        return 0;
    }

    private int checkSetUpPatternCode(String code) {
        if (!isConfirmCode) {
            confirmCode(code);
        } else {
            if (code.equals(codeConfirm)) {
                setUpCodeSuccess();
                return PatternLockView.CODE_PASSWORD_CORRECT;
            } else {
                setUpCodeFailed();
                return PatternLockView.CODE_PASSWORD_ERROR;
            }
        }
        return 0;
    }

    private void changeCodeFailed() {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(headerLockviewBinding.tvMessage);
        isConfirmCode = false;
        headerLockviewBinding.tvMessage.setText(getResources().getString(R.string.enter_new_pattern_code));
    }

    private void changeCodeSuccess(String code) {
        PreferencesHelper.setPatternCode(code);
        toast(getResources().getString(R.string.message_change_pattern_code_success));
        requireActivity().setResult(Activity.RESULT_OK);
        requireActivity().finish();
    }

    private int checkPatternCodeFailed(String code) {
        binding.tvForgotPassword.setVisibility(View.VISIBLE);
        captureWhenFailed(code);
        YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(headerLockviewBinding.tvMessage);
        return PatternLockView.CODE_PASSWORD_ERROR;
    }

    private int checkPatternCodeSuccess() {
        App.getInstace().setForceLockScreen(true);
        requireActivity().setResult(Activity.RESULT_OK);
        requireActivity().finish();
        return PatternLockView.CODE_PASSWORD_CORRECT;
    }

    private int checkPatternCodeAntiTheftSuccess() {
        if (Toolbox.isAppOnForeground(getBaseActivity())) {
            App.getInstace().setForceLockScreen(true);
        }

        PreferencesHelper.putInt(PreferencesHelper.DETECTION_TYPE, Config.DETECTION_NONE);
        if (BackgroundManager.getInstance(getActivity()).isServiceRunning(AntiTheftService.class)) {
            Intent intent = new Intent(getActivity(), AntiTheftService.class);
            intent.setAction(Config.ActionIntent.ACTION_STOP_ANTI_THEFT);
            getActivity().startService(intent);
        }
        requireActivity().finish();
        return PatternLockView.CODE_PASSWORD_CORRECT;
    }

    private int checkPatternCodeSwitchSuccess() {
        App.getInstace().setForceLockScreen(true);
        navigate(R.id.action_nav_pattern_code_to_nav_pin_code);
        return PatternLockView.CODE_PASSWORD_CORRECT;
    }

    private void confirmCode(String code) {
        isConfirmCode = true;
        codeConfirm = code;
        headerLockviewBinding.tvMessage.setText(getResources().getString(R.string.message_confirm_pattern_code));
    }

    private void setUpCodeFailed() {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(headerLockviewBinding.tvMessage);
        isConfirmCode = false;
        headerLockviewBinding.tvMessage.setText(getResources().getString(R.string.enter_new_pattern_code));
    }

    private void setUpCodeSuccess() {
        toast(getResources().getString(R.string.message_confirm_pattern_code_success));
        gotoQuestion();
    }

    private void gotoQuestion() {
        Bundle bundle = new Bundle();
        bundle.putString(Config.KeyBundle.KEY_PATTERN_CODE, codeConfirm);
        bundle.putInt(Config.KeyBundle.KEY_CHANGE_QUESTION, Config.DataBundle.DATA_FIRST_SETUP_QUESTION);
        navigate(R.id.action_nav_pattern_code_to_nav_question, bundle);
    }

    private void captureWhenFailed(String code) {
        if (!PreferencesHelper.getBoolean(PreferencesHelper.INTRUDER_SELFIE, false))
            return;
        int entries = PreferencesHelper.getInt(PreferencesHelper.INTRUDER_SELFIE_ENTRIES, Config.DEFAULT_INTRUDER_SELFIE);
        countEntries++;
        if (countEntries >= entries) {
            new Thread(() -> {
                camera2Controller.takePicture(getContext(), code);
            }).start();
            countEntries = 0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (camera2Controller != null) {
            camera2Controller.releaseCamera();
        }
        if (fingerprintManager!=null){
            fingerprintManager.stopListeningAuthentication();
        }
    }

    @Override
    protected FragmentPatternCodeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentPatternCodeBinding.inflate(inflater, container, false);
    }

    @Override
    protected int getTitleFragment() {
        return R.string.change_pin_code;
    }
}

