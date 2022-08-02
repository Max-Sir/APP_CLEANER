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
import com.security.applock.databinding.LayoutPinCodeBinding;
import com.security.applock.ui.forgotpassword.ForgotPasswordActivity;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;
import com.security.applock.utils.SystemUtil;
import com.security.applock.utils.Toolbox;
import com.security.applock.utils.ViewUtils;

public class PinCodeLayout extends ConstraintLayout {
    private LayoutPinCodeBinding binding;
    private LayoutHeaderLockviewBinding headerLockviewBinding;
    private CheckPasswordCodeListener listener;

    public void setListener(CheckPasswordCodeListener listener) {
        this.listener = listener;
    }

    public PinCodeLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PinCodeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        binding = LayoutPinCodeBinding.inflate(LayoutInflater.from(context), this, true);
        headerLockviewBinding = LayoutHeaderLockviewBinding.bind(binding.llHeaderLockview.getRoot());
        binding.tvForgotPassword.setPaintFlags(binding.tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
            if (pin.equals(PreferencesHelper.getPinCode())) {
                listener.onCheck(CheckPasswordCodeListener.State.SUCCESS, pin);
            } else {
                binding.tvForgotPassword.setVisibility(View.VISIBLE);
                binding.pinView.onPinCodeFailed();
                binding.pinView.setText(Config.TextPassword.EMPTY);
                listener.onCheck(CheckPasswordCodeListener.State.FAILED, pin);
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(headerLockviewBinding.tvMessage);
            }
            binding.pinView.setText(Config.TextPassword.EMPTY);
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
        SystemUtil.gotoHomeLauncher(getContext());
        if (listener != null)
            listener.onCheck(CheckPasswordCodeListener.State.SUCCESS, "");
        Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
        Toolbox.startActivityAllStage(getContext(), intent);
    }

    public void cleanPinCode() {
        binding.pinView.setText(Config.TextPassword.EMPTY);
    }

    public void setInforApplication(String packageName) {
        try {
            binding.tvForgotPassword.setVisibility(GONE);
            headerLockviewBinding.imvIcon.setImageDrawable(Toolbox.getdIconApplication(getContext(), packageName));
            headerLockviewBinding.tvMessage.setText(Toolbox.getdNameApplication(getContext(), packageName));
            int theme = PreferencesHelper.getInt(PreferencesHelper.SETTING_THEME, R.drawable.bg_theme_defatult);

            if (theme == R.drawable.bg_theme_defatult) {
                binding.tvForgotPassword.setTextColor(Color.WHITE);
                binding.imBackground.setVisibility(GONE);
                binding.imContent.setVisibility(VISIBLE);
                headerLockviewBinding.imHeaderLockview.setVisibility(VISIBLE);
                headerLockviewBinding.llHeaderLockview.setBackgroundResource(R.color.colorPrimaryDark);
            } else if (theme == R.drawable.bg_theme_blur) {
                binding.tvForgotPassword.setTextColor(Color.WHITE);
                Drawable iconApp = Toolbox.getdIconApplication(getContext(), packageName);
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

