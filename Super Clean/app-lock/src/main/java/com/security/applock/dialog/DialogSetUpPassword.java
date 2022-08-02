package com.security.applock.dialog;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.security.applock.databinding.DialogSetupPasswordBinding;

import java.util.HashMap;

public class DialogSetUpPassword extends BaseDialog<DialogSetupPasswordBinding, DialogSetUpPassword.ExtendBuilder> {


    public DialogSetUpPassword(DialogSetUpPassword.ExtendBuilder builder) {
        super(builder);
    }

    @Override
    protected DialogSetupPasswordBinding getViewBinding() {
        return DialogSetupPasswordBinding.inflate(LayoutInflater.from(getContext()));
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initControl() {
    }

    @Override
    protected TextView getTitle() {
        return binding.tvTitle;
    }

    @Override
    protected TextView getPositiveButton() {
        return binding.tvPositive;
    }

    @Override
    protected void handleClickPositiveButton(HashMap<String, Object> datas) {
        super.handleClickPositiveButton(datas);
    }

    public static class ExtendBuilder extends BuilderDialog {
        @Override
        public BaseDialog build() {
            return new DialogSetUpPassword(this);
        }

    }
}

