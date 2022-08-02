package com.security.applock.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.security.applock.App;
import com.security.applock.databinding.ActivitySplashLockBinding;
import com.security.applock.ui.BaseActivity;
import com.security.applock.ui.main.MainActivity;
import com.security.applock.ui.password.PasswordActivity;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;
import com.security.applock.utils.SystemUtil;

import java.util.List;

public class SplashActivity extends BaseActivity<ActivitySplashLockBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
//        int idIconCurrent = PreferencesHelper.getInt(PreferencesHelper.SETTING_APP_MASK, 0);
//        Glide.with(this).load(Config.lstIconApp[idIconCurrent].getIconPreview()).into(binding.imLogo);
//        binding.tvAppname.setText(Config.lstIconApp[idIconCurrent].getNameDisplay());
        handlerScreen();
    }

    private void handlerScreen() {
        if (TextUtils.isEmpty(PreferencesHelper.getQuestionAnser())) {
            gotoSetUpPassword();
      //      initFirstInstallApp();
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }

    private void initFirstInstallApp() {
        List<String> listAppLaucher = SystemUtil.getHomesLauncher(this);
        PreferencesHelper.setListAppKidZone(listAppLaucher);
    }

    private void gotoSetUpPassword() {
        Intent intent = new Intent(this, PasswordActivity.class);
        intent.putExtra(Config.KeyBundle.KEY_FIRST_SETUP_PASS, true);
        intent.setAction(Config.ActionIntent.ACTION_SET_UP_PATTERN_CODE);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected ActivitySplashLockBinding getViewBinding() {
        return ActivitySplashLockBinding.inflate(LayoutInflater.from(SplashActivity.this));
    }

    @Override
    protected void initControl() {

    }
}
