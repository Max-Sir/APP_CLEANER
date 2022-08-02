package com.security.applock.ui.guildPermission;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;

import com.security.applock.R;
import com.security.applock.databinding.ActivityGuideSettingBinding;
import com.security.applock.ui.BaseActivity;


public class GuildPermissionActivity extends BaseActivity<ActivityGuideSettingBinding> {

    private static final String PERMISSION_NAME = "permission name";

    public static void openActivityGuildPermission(Context mContext, String permissionName) {
        Intent mIntent = new Intent(mContext, GuildPermissionActivity.class);
        mIntent.putExtra(PERMISSION_NAME, permissionName);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void initView() {
        String permissionName = getIntent().getStringExtra(PERMISSION_NAME);
        if (permissionName.equals(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)) {
            binding.tvContent.setText(getString(R.string.find_and_grant, getString(R.string.app_name), getString(R.string.floatint_windown_per)));
        } else if (permissionName.equals(Settings.ACTION_ACCESSIBILITY_SETTINGS)) {
            binding.tvContent.setText(getString(R.string.find_and_grant, getString(R.string.app_name), getString(R.string.accessibility_permission)));
        } else if (permissionName.equals(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) {
            binding.tvContent.setText(getString(R.string.find_and_grant, getString(R.string.app_name), getString(R.string.listener_notification_per)));
        } else if (permissionName.equals(Settings.ACTION_MANAGE_WRITE_SETTINGS)) {
            binding.tvContent.setText(getString(R.string.find_and_grant, getString(R.string.app_name), getString(R.string.write_setting_per)));
        } else if (permissionName.equals(Settings.ACTION_USAGE_ACCESS_SETTINGS)) {
            binding.tvContent.setText(getString(R.string.find_and_grant, getString(R.string.app_name), getString(R.string.usage_access_permission)));
        }
    }

    @Override
    protected void initControl() {
        binding.icClose.setOnClickListener(view -> finish());
        binding.layoutPadding.setOnClickListener(view -> finish());
    }

    @Override
    protected ActivityGuideSettingBinding getViewBinding() {
        return ActivityGuideSettingBinding.inflate(LayoutInflater.from(this));
    }

}
