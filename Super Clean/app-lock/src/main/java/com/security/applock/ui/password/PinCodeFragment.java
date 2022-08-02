package com.security.applock.ui.password;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.security.applock.App;
import com.security.applock.R;
import com.security.applock.databinding.FragmentPinCodeBinding;
import com.security.applock.databinding.LayoutHeaderLockviewBinding;
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

public class PinCodeFragment extends BaseFragment<FragmentPinCodeBinding> {
    private Boolean isConfirmPinCode = false;
    private String pinConfirm = "";
    private String action = "";
    private Camera2Controller camera2Controller;
    private int countEntries = 0;
    private LayoutHeaderLockviewBinding headerLockviewBinding;
    private FingerprintManager fingerprintManager;

    @Override
    protected void initView() {
        binding.tvForgotPassword.setPaintFlags(binding.tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        headerLockviewBinding = LayoutHeaderLockviewBinding.bind(binding.llHeaderLockview.getRoot());
        camera2Controller = new Camera2Controller(binding.textureview, getActivity());
        binding.pinView.setAnimationEnable(true);
        binding.pinView.setText(Config.TextPassword.EMPTY);
        action = requireActivity().getIntent().getAction() != null ? requireActivity().getIntent().getAction() : "";
        switch (action) {
            // action switch to pin code
            case Config.ActionIntent.ACTION_CHANGE_PIN_CODE:
                headerLockviewBinding.tvMessage.setText(getResources().getString(R.string.enter_pattern_code));
                break;
            case Config.ActionIntent.ACTION_SWITCH_TO_PIN_CODE:
            case Config.ActionIntent.ACTION_SET_UP_PIN_CODE_WHEN_CHANGE:
                headerLockviewBinding.tvMessage.setText(getResources().getString(R.string.enter_new_pin_code));
                break;
            case Config.ActionIntent.ACTION_CHECK_PASSWORD_FROM_SERVICE:
                String packageName = requireActivity().getIntent().getStringExtra(Config.KeyBundle.KEY_PACKAGE_NAME);
                try {
                    headerLockviewBinding.imvIcon.setImageDrawable(Toolbox.getdIconApplication(getContext(), packageName));
                    headerLockviewBinding.tvMessage.setText(Toolbox.getdNameApplication(getContext(), packageName));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                initFingerprint();
                break;
            default:
                initFingerprint();
                headerLockviewBinding.tvMessage.setText(getResources().getString(R.string.enter_pin_code));
                break;
        }
    }

    private void initFingerprint() {
        if (!SystemUtil.hasFingerprint(requireContext()) || !PreferencesHelper.getBoolean(PreferencesHelper.FINGERPRINT_UNLOCK, false))
            return;

        fingerprintManager = new FingerprintManager(getActivity(), new FingerprintManager.FingerListener() {
            @Override
            public void onSuccessListener() {
                onPinCodeAntiTheftSuccess();
            }

            @Override
            public void onFailsListener() {

            }
        });
        fingerprintManager.initFinger();
    }

    @Override
    protected void initControl() {
        binding.btn0.setOnClickListener(v -> binding.pinView.append(Config.TextPassword.ZERO));
        binding.btn1.setOnClickListener(v -> binding.pinView.append(Config.TextPassword.ONE));
        binding.btn2.setOnClickListener(v -> binding.pinView.append(Config.TextPassword.TWO));
        binding.btn3.setOnClickListener(v -> binding.pinView.append(Config.TextPassword.THREE));
        binding.btn4.setOnClickListener(v -> binding.pinView.append(Config.TextPassword.FOUR));
        binding.btn5.setOnClickListener(v -> binding.pinView.append(Config.TextPassword.FIVE));
        binding.btn6.setOnClickListener(v -> binding.pinView.append(Config.TextPassword.SIX));
        binding.btn7.setOnClickListener(v -> binding.pinView.append(Config.TextPassword.SEVEN));
        binding.btn8.setOnClickListener(v -> binding.pinView.append(Config.TextPassword.EIGHT));
        binding.btn9.setOnClickListener(v -> binding.pinView.append(Config.TextPassword.NINE));
        binding.btnX.setOnClickListener(v -> binding.pinView.setText(Config.TextPassword.EMPTY));
        binding.pinView.setOnCheckPinCode(pin -> {
            switch (action) {
                // action switch pattern code -> pin code
                case Config.ActionIntent.ACTION_CHANGE_PIN_CODE:
                    checkPinCodeWhenChange(pin);
                    break;
                case Config.ActionIntent.ACTION_SET_UP_PIN_CODE_WHEN_CHANGE:
                    checkSetUpPinCode(pin);
                    break;
                case Config.ActionIntent.ACTION_SWITCH_TO_PIN_CODE:
                    checkSetUpPinCode(pin);
                    break;
                // action switch pin code -> pattern code
                case Config.ActionIntent.ACTION_SWITCH_TO_PATTERN_CODE:
                    checkSwitchPinCode(pin);
                    break;
                case Config.ActionIntent.ACTION_CHECK_PIN_CODE:
                    checkPinCode(pin);
                    break;
                case Config.ActionIntent.ACTION_CHECK_PASSWORD_ANTI_THEFT:
                    checkPinCodeAntiTheft(pin);
                    break;
                case Config.ActionIntent.ACTION_CHECK_PASSWORD_FROM_SERVICE:
                    checkPinCodeUnLockApp(pin);
                    break;
                default:
                    checkPinCode(pin);
                    break;
            }
        });

        binding.tvForgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
        });
    }

    private void checkPinCodeUnLockApp(String pin) {
        if (pin.equals(PreferencesHelper.getPinCode())) {
            onPinCodeUnLockAppSuccess();
        } else {
            onCheckPinCodeFailed(pin);
        }
    }

    private void onPinCodeUnLockAppSuccess() {
        App.getInstace().setForceLockScreen(true);
        requireActivity().finish();
    }

    private void checkPinCode(String pin) {
        if (pin.equals(PreferencesHelper.getPinCode())) {
            onPinCodeSuccess();
        } else {
            onCheckPinCodeFailed(pin);
        }
    }

    private void checkPinCodeWhenChange(String pin) {
        if (pin.equals(PreferencesHelper.getPinCode())) {
            onPinCodeSuccessWhenChange();
        } else {
            onCheckPinCodeFailed(pin);
        }
    }

    private void onPinCodeSuccessWhenChange() {
        requireActivity().getIntent().setAction(Config.ActionIntent.ACTION_SET_UP_PIN_CODE_WHEN_CHANGE);
        initView();
    }

    private void checkPinCodeAntiTheft(String pin) {
        if (pin.equals(PreferencesHelper.getPinCode())) {
            onPinCodeAntiTheftSuccess();
        } else {
            onCheckPinCodeFailed(pin);
        }
    }

    private void onCheckPinCodeFailed(String pin) {
        binding.tvForgotPassword.setVisibility(View.VISIBLE);
        captureWhenFailed(pin);
        YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(headerLockviewBinding.tvMessage);
        binding.pinView.onPinCodeFailed();
        binding.pinView.setText(Config.TextPassword.EMPTY);
        isConfirmPinCode = false;
        pinConfirm = "";
//        binding.tvMessage.setText(getResources().getString(R.string.error_message_check_pin_code));
//        toast(getResources().getString(R.string.error_message_check_pin_code));
    }

    private void onPinCodeSuccess() {
        App.getInstace().setForceLockScreen(true);
        requireActivity().finish();
    }

    private void onPinCodeAntiTheftSuccess() {
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
    }

    // action switch pin code -> pattern code
    private void checkSwitchPinCode(String pin) {
        if (pin.equals(PreferencesHelper.getPinCode())) {
            onPinCodeSwitchSuccess();
        } else {
            onPinCodeFailed();
        }
    }

    private void onPinCodeSwitchSuccess() {
        navigate(R.id.action_nav_pin_code_to_nav_pattern_code);
        toast(getResources().getString(R.string.message_confirm_pin_code_success));
    }
    // end action switch pin code -> pattern code

    // action switch pattern code -> pin code
    private void checkSetUpPinCode(String pin) {
        if (isConfirmPinCode) {
            checkConfirmSetUpPinCode(pin);
        } else {
            gotoConfirmPinCode(pin);
        }
    }

    private void checkConfirmSetUpPinCode(String pin) {
        if (pin.equals(pinConfirm)) {
            onSetUpPinCodeSwitchSuccess(pin);
        } else {
            onPinCodeFailed();
        }
    }

    private void onSetUpPinCodeSwitchSuccess(String pin) {
        PreferencesHelper.setPinCode(pin);
        toast(getResources().getString(R.string.message_confirm_pin_code_success));
        requireActivity().setResult(Activity.RESULT_OK);
        requireActivity().finish();
    }
    // end action switch pattern code -> pin code

    private void onPinCodeFailed() {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(headerLockviewBinding.tvMessage);
        binding.pinView.onPinCodeFailed();
        binding.pinView.setText(Config.TextPassword.EMPTY);
        isConfirmPinCode = false;
        pinConfirm = "";
        headerLockviewBinding.tvMessage.setText(getResources().getString(R.string.error_message_confirm_pin_code));
    }

    private void gotoConfirmPinCode(String pin) {
        isConfirmPinCode = true;
        pinConfirm = pin;
        binding.pinView.setText(Config.TextPassword.EMPTY);
        headerLockviewBinding.tvMessage.setText(getResources().getString(R.string.message_confirm_pin_code));
    }

    private void captureWhenFailed(String pin) {
        if (!PreferencesHelper.getBoolean(PreferencesHelper.INTRUDER_SELFIE, false))
            return;
        int entries = PreferencesHelper.getInt(PreferencesHelper.INTRUDER_SELFIE_ENTRIES, Config.DEFAULT_INTRUDER_SELFIE);
        countEntries++;
        countEntries++;
        if (countEntries >= entries) {
            new Thread(() -> {
                camera2Controller.takePicture(getContext(), pin);
            }).start();
            countEntries = 0;
        }
    }

    @Override
    protected FragmentPinCodeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentPinCodeBinding.inflate(inflater, container, false);
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
    protected int getTitleFragment() {
        return R.string.change_pin_code;
    }
}
