package com.security.applock.dialog;

import android.Manifest;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.security.applock.R;
import com.security.applock.databinding.DialogAskPermissionLockBinding;

public class DialogAskPermission extends BaseDialog<DialogAskPermissionLockBinding, DialogAskPermission.ExtendBuilder> {

    private ExtendBuilder extendBuilder;

    public DialogAskPermission(DialogAskPermission.ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected DialogAskPermissionLockBinding getViewBinding() {
        return DialogAskPermissionLockBinding.inflate(LayoutInflater.from(getContext()));
    }

    @Override
    protected void initView() {
        super.initView();
        String permissionName = extendBuilder.permissionName;
        if (permissionName.equals(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)) {
            binding.tvContent.setText(getString(R.string.overlay_permission));
        } else if (permissionName.equals(Settings.ACTION_ACCESSIBILITY_SETTINGS)) {
            binding.tvContent.setText(getString(R.string.accessibility_setting_permission));
        } else if (permissionName.equals(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) {
            binding.tvContent.setText(getString(R.string.listen_notification_permission));
        } else if (permissionName.equals(Settings.ACTION_MANAGE_WRITE_SETTINGS)) {
            binding.tvContent.setText(getString(R.string.write_setting_permission));
        } else if (permissionName.equals(Settings.ACTION_USAGE_ACCESS_SETTINGS)) {
            binding.tvContent.setText(getString(R.string.grant_app_permission));
        } else if (permissionName.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            binding.tvContent.setText(getString(R.string.external_storage_permission));
        } else if (permissionName.equals(Manifest.permission.CAMERA)) {
            binding.tvContent.setText(getString(R.string.camera_permission));
        }
    }

    @Override
    protected TextView getPositiveButton() {
        return binding.tvPositive;
    }

    @Override
    protected void initControl() {

    }

    public static class ExtendBuilder extends BuilderDialog {

        private String permissionName;

        @Override
        public BaseDialog build() {
            return new DialogAskPermission(this);
        }

        public ExtendBuilder setPermissionName(String permissionName) {
            this.permissionName = permissionName;
            return this;
        }
    }
}
