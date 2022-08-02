package com.security.applock.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.security.applock.R;
import com.security.applock.databinding.LayoutHeaderLockviewBinding;
import com.security.applock.databinding.LayoutPatternCodeBinding;
import com.security.applock.ui.forgotpassword.ForgotPasswordActivity;
import com.security.applock.utils.PreferencesHelper;
import com.security.applock.utils.SystemUtil;
import com.security.applock.utils.Toolbox;
import com.security.applock.utils.ViewUtils;

public class PatternLockLayout extends ConstraintLayout {
    private LayoutPatternCodeBinding binding;
    private LayoutHeaderLockviewBinding headerLockviewBinding;
    private CheckPasswordCodeListener listener;

    public void setListener(CheckPasswordCodeListener listener) {
        this.listener = listener;
    }

    public PatternLockLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PatternLockLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        binding = LayoutPatternCodeBinding.inflate(LayoutInflater.from(context), this, true);
        headerLockviewBinding = LayoutHeaderLockviewBinding.bind(binding.llHeaderLockview.getRoot());
        binding.tvForgotPassword.setPaintFlags(binding.tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.patternCodeWhite.setFinishInterruptable(false);
        binding.patternCodeWhite.setCallBack(password -> {
            if (password.string.equals(PreferencesHelper.getPatternCode())) {
                return checkPatternCodeSuccess(password.string);
            } else {
                return checkPatternCodeFailed(password.string);
            }
        });
        binding.patternCodeDefault.setCallBack(password -> {
            if (password.string.equals(PreferencesHelper.getPatternCode())) {
                return checkPatternCodeSuccess(password.string);
            } else {
                return checkPatternCodeFailed(password.string);
            }
        });
//        headerLockviewBinding.imMenu.setVisibility(VISIBLE);

        headerLockviewBinding.imMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), headerLockviewBinding.imMenu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_lockview, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                openQuestionScreen();
                return true;
            });
            popupMenu.show();
        });

        binding.tvForgotPassword.setOnClickListener(view -> {
            openQuestionScreen();
        });
    }

    private void openQuestionScreen() {
        binding.tvForgotPassword.setVisibility(GONE);
        SystemUtil.gotoHomeLauncher(getContext());
        if (listener != null)
            listener.onCheck(CheckPasswordCodeListener.State.SUCCESS, "");
        Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
        Toolbox.startActivityAllStage(getContext(), intent);
    }

    private int checkPatternCodeSuccess(String passInput) {
        if (listener != null)
            listener.onCheck(CheckPasswordCodeListener.State.SUCCESS, passInput);
        return PatternLockView.CODE_PASSWORD_ERROR;
    }

    private int checkPatternCodeFailed(String passInput) {
        binding.tvForgotPassword.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(headerLockviewBinding.tvMessage);
        if (listener != null)
            listener.onCheck(CheckPasswordCodeListener.State.FAILED, passInput);
        return PatternLockView.CODE_PASSWORD_ERROR;
    }

    public void setInforApplication(String packageName) {
        try {
            binding.tvForgotPassword.setVisibility(GONE);
            headerLockviewBinding.imvIcon.setImageDrawable(Toolbox.getdIconApplication(getContext(), packageName));
            headerLockviewBinding.tvMessage.setText(Toolbox.getdNameApplication(getContext(), packageName));
            int theme = PreferencesHelper.getInt(PreferencesHelper.SETTING_THEME, R.drawable.bg_theme_defatult);

            if (theme == R.drawable.bg_theme_defatult) {
                binding.tvForgotPassword.setTextColor(Color.WHITE);
                binding.patternCodeDefault.setVisibility(VISIBLE);
                binding.patternCodeWhite.setVisibility(GONE);
                binding.imBackground.setVisibility(GONE);
                binding.imContent.setVisibility(VISIBLE);
                headerLockviewBinding.imHeaderLockview.setVisibility(VISIBLE);
                headerLockviewBinding.llHeaderLockview.setBackgroundResource(R.color.colorPrimaryDark);
            } else if (theme == R.drawable.bg_theme_blur) {
                binding.tvForgotPassword.setTextColor(Color.WHITE);
                Drawable iconApp = Toolbox.getdIconApplication(getContext(), packageName);
                binding.patternCodeDefault.setVisibility(GONE);
                binding.patternCodeWhite.setVisibility(VISIBLE);
                binding.imBackground.setVisibility(VISIBLE);
                binding.imContent.setVisibility(GONE);
                headerLockviewBinding.imHeaderLockview.setVisibility(GONE);
                headerLockviewBinding.llHeaderLockview.setBackgroundResource(android.R.color.transparent);
                binding.imBackground.setBackgroundDrawable(iconApp);
                binding.imBackground.getViewTreeObserver().addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                binding.imBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                                binding.imBackground.buildDrawingCache();
                                int width = binding.imBackground.getWidth(), height = binding.imBackground.getHeight();
                                if (width != 0 || height != 0) {
                                    Bitmap bmp = ViewUtils.drawableToBitmap(iconApp, width, height);
                                    ViewUtils.blur(getContext(), ViewUtils.big(bmp), binding.imBackground, width, height);
                                }
                                return true;
                            }
                        });

            } else {
                binding.tvForgotPassword.setTextColor(Color.WHITE);
                binding.patternCodeDefault.setVisibility(GONE);
                binding.patternCodeWhite.setVisibility(VISIBLE);
                binding.imBackground.setVisibility(VISIBLE);
                binding.imBackground.setBackgroundResource(theme);
                binding.imContent.setVisibility(GONE);
                headerLockviewBinding.imHeaderLockview.setVisibility(GONE);
                headerLockviewBinding.llHeaderLockview.setBackgroundResource(android.R.color.transparent);
            }
        } catch (Exception e) {

        }
    }

}

