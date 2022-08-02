package com.security.applock.widget;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.security.applock.App;
import com.security.applock.databinding.LayoutLockviewManagerBinding;
import com.security.applock.utils.Camera2Controller;
import com.security.applock.utils.Config;
import com.security.applock.utils.FingerprintManager;
import com.security.applock.utils.PreferencesHelper;
import com.security.applock.utils.SystemUtil;

public class LockViewWindowManager extends ConstraintLayout implements CheckPasswordCodeListener {

    private LayoutLockviewManagerBinding binding;
    private Context context;
    private PasswordConfirmListener passwordConfirmListener;
    private FingerprintManager fingerprintManager;
    private String currentPakageName = "";
    private Camera2Controller camera2Controller;
    private int countEntries = 0;

    public interface PasswordConfirmListener {
        void onSuccess(String currentPackage);

        void onFails(String passInput);
    }

    private void initFingerprint() {
        if (!SystemUtil.hasFingerprint(getContext()) || !PreferencesHelper.getBoolean(PreferencesHelper.FINGERPRINT_UNLOCK, false))
            return;

        fingerprintManager = new FingerprintManager(context, new FingerprintManager.FingerListener() {
            @Override
            public void onSuccessListener() {
                if (passwordConfirmListener != null) {
                    passwordConfirmListener.onSuccess(currentPakageName);
                }
            }

            @Override
            public void onFailsListener() {

            }
        });
        fingerprintManager.initFinger();
    }

    public void setPasswordConfirmListener(PasswordConfirmListener passwordConfirmListener) {
        this.passwordConfirmListener = passwordConfirmListener;
    }

    public LockViewWindowManager(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
        camera2Controller = new Camera2Controller(binding.textureview, context);
    }

    private void initView() {
        binding = LayoutLockviewManagerBinding.inflate(LayoutInflater.from(context), this, true);
        setVisibility(GONE);
        initListener();
    }

    private void initListener() {
        binding.patternLockLayout.setListener(this);
        binding.pinCodeLayout.setListener(this);
    }

    public void showhideViewPassword(boolean isShow, String packageName) {
        currentPakageName = packageName;

        binding.patternLockLayout.setVisibility(GONE);
        binding.pinCodeLayout.setVisibility(GONE);

        if (!isShow) {
            setVisibility(GONE);
            binding.patternLockLayout.setVisibility(GONE);
            binding.pinCodeLayout.setVisibility(GONE);
            binding.pinCodeLayout.cleanPinCode();
            if (fingerprintManager!=null){
                fingerprintManager.stopListeningAuthentication();
            }
        } else {
            countEntries = 0;
            setVisibility(VISIBLE);
            if (PreferencesHelper.isPatternCode()) {
                binding.patternLockLayout.setVisibility(VISIBLE);
                binding.patternLockLayout.setInforApplication(packageName);
            } else {
                binding.pinCodeLayout.setVisibility(VISIBLE);
                binding.pinCodeLayout.setInforApplication(packageName);
            }
            initFingerprint();
        }
    }

    @Override
    public void onCheck(State state, String passInput) {
        if (passwordConfirmListener != null) {
            if (state == State.FAILED) {
                passwordConfirmListener.onFails(passInput);
                new Handler().postDelayed(() -> new Thread(() -> {
                   captureWhenFailed(passInput);
                }).start(), 500);
            } else
                passwordConfirmListener.onSuccess(currentPakageName);
        }
    }

    private void captureWhenFailed(String pin) {
        if (!PreferencesHelper.getBoolean(PreferencesHelper.INTRUDER_SELFIE, false))
            return;
        int entries = PreferencesHelper.getInt(PreferencesHelper.INTRUDER_SELFIE_ENTRIES, Config.DEFAULT_INTRUDER_SELFIE);
        countEntries++;
        if (countEntries >= entries && camera2Controller != null) {
            camera2Controller.takePicture(getContext(), pin);
            countEntries = 0;
        }
    }

}
