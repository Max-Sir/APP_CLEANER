package com.lubuteam.sellsourcecode.supercleaner.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewbinding.ViewBinding;


public abstract class BaseDialog2<BD extends ViewBinding, B extends BuilderDialog2> extends DialogFragment {

    protected B builder;
    protected BD binding;

    public BaseDialog2(B builder) {
        this.builder = builder;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        String t = getClass().getSimpleName();
        if (manager.findFragmentByTag(t) == null) {
            super.show(manager, t);
        }
    }

    protected abstract BD getViewBinding();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
        binding = getViewBinding();
        dialogBuilder.setView(binding.getRoot());
        initView();
        initControl();
        return dialogBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.7f;
            windowParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
            window.setAttributes(windowParams);
            dialog.setCancelable(builder.cancelable);
            dialog.setCanceledOnTouchOutside(builder.canOntouchOutside);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    protected void initView() {
        if (!TextUtils.isEmpty(builder.title) && getTitle() != null) {
            getTitle().setText(builder.title);
        }
    }

    protected abstract void initControl();

    protected TextView getTitle() {
        return null;
    }

}
