package com.security.applock.ui.advanced;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;

import com.security.applock.App;
import com.security.applock.R;
import com.security.applock.databinding.FragmentAdvanceProtectionBinding;
import com.security.applock.service.BackgroundManager;
import com.security.applock.service.KeepLiveAccessibilityService;
import com.security.applock.ui.BaseActivity;
import com.security.applock.ui.BaseFragment;
import com.security.applock.utils.Config;
import com.security.applock.utils.SystemUtil;
import com.security.applock.widget.MenuFunction;


public class FragmentAdvancedProtection extends BaseFragment<FragmentAdvanceProtectionBinding> {

    @Override
    protected void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        setStatusIcon(binding.funcUseCamera,
                ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
        setStatusIcon(binding.funcSaving, BackgroundManager.getInstance(getActivity()).isServiceRunning(KeepLiveAccessibilityService.class));
        setStatusIcon(binding.funcStable, SystemUtil.isUsageAccessAllowed(getActivity()));
        setStatusIcon(binding.funcPopup, Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(getActivity()));
    }

    private void setStatusIcon(MenuFunction menuFunction, boolean isActive) {
//        int color = getResources().getColor(isActive ? R.color.color_2D9CFF : R.color.color_828282);
        int color = getResources().getColor(isActive ? R.color.color_a5a5ff : android.R.color.white);
        menuFunction.setTintIcon2(color);
    }

    @Override
    protected void initControl() {
        binding.funcUseCamera.setItemClickListener(() -> {
            getBaseActivity().askPermissionStorage(() -> {
                getBaseActivity().askPermissionCamera(() -> {
                    App.getInstace().setForceLockScreen(true);
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                    startActivity(intent);
                    return null;
                });
                return null;
            });
        });
        binding.funcSaving.setItemClickListener(() -> {
            if (BackgroundManager.getInstance(getActivity()).isServiceRunning(KeepLiveAccessibilityService.class)) {
                App.getInstace().setForceLockScreen(true);
                startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), BaseActivity.REQUEST_ACCESSIBILITY_SETTINGS);
            } else {
                getBaseActivity().requestDetectHome();
            }
        });
        binding.funcStable.setItemClickListener(() -> {
            if (SystemUtil.isUsageAccessAllowed(getActivity())) {
                App.getInstace().setForceLockScreen(true);
                startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), Config.PERMISSIONS_USAGE);
            } else {
                getBaseActivity().askPermissionUsageSetting(() -> null);
            }
        });

        binding.funcPopup.setItemClickListener(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(getActivity())) {
                App.getInstace().setForceLockScreen(true);
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, Config.PERMISSIONS_DRAW_APPICATION);
            } else {
                getBaseActivity().checkdrawPermission(() -> null);
            }
        });
    }

    @Override
    protected FragmentAdvanceProtectionBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentAdvanceProtectionBinding.inflate(inflater, container, false);
    }

    @Override
    protected int getTitleFragment() {
        return R.string.advanced_protection;
    }
}
