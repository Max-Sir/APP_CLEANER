package com.lubuteam.sellsourcecode.supercleaner.dialog;

import android.Manifest;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.lubuteam.sellsourcecode.supercleaner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogAskPermission extends DialogFragment {

    @BindView(R.id.tv_content)
    TextView tvContent;

    private SuccessListener mSuccessListener;
    private String permissionName;

    public interface SuccessListener {
        void onSuccess();
    }

    public static DialogAskPermission getInstance(String permissionName, SuccessListener mSuccessListener) {
        DialogAskPermission mDialogAskPermission = new DialogAskPermission();
        mDialogAskPermission.mSuccessListener = mSuccessListener;
        mDialogAskPermission.permissionName = permissionName;
        return mDialogAskPermission;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        String t = getClass().getSimpleName();
        if (manager.findFragmentByTag(t) == null) {
            super.show(manager, t);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_ask_permission, null);
        ButterKnife.bind(this, mView);
        dialogBuilder.setView(mView);
        try {
            initData();
        } catch (Exception e) {

        }
        return dialogBuilder.create();
    }

    private void initData() {
        if (permissionName.equals(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)) {
            tvContent.setText(getString(R.string.overlay_permission));
        } else if (permissionName.equals(Settings.ACTION_ACCESSIBILITY_SETTINGS)) {
            tvContent.setText(getString(R.string.accessibility_setting_permission));
        } else if (permissionName.equals(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) {
            tvContent.setText(getString(R.string.listen_notification_permission));
        } else if (permissionName.equals(Settings.ACTION_MANAGE_WRITE_SETTINGS)) {
            tvContent.setText(getString(R.string.write_setting_permission));
        } else if (permissionName.equals(Settings.ACTION_USAGE_ACCESS_SETTINGS)) {
            tvContent.setText(getString(R.string.grant_app_permission));
        } else if (permissionName.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            tvContent.setText(getString(R.string.external_storage_permission));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.7f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
//            windowParams.windowAnimations = R.style.DialogAnimation;
            window.setAttributes(windowParams);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
//            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @OnClick({R.id.tv_action_1})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.tv_action_1:
                dismiss();
                if (mSuccessListener != null)
                    mSuccessListener.onSuccess();
                break;
        }
    }

}
