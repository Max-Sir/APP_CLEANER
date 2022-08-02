package com.lubuteam.sellsourcecode.supercleaner.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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

import static android.view.WindowManager.*;

public class DialogSelection extends DialogFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_action_1)
    TextView tvAction1;
    @BindView(R.id.tv_action_2)
    TextView tvAction2;

    private Builder mBuilder;

    public interface OnClickAction1Listener {
        void onAction1Click(DialogSelection dialog);
    }

    public interface OnClickAction2Listener {
        void onAction2Click(DialogSelection dialog);
    }

    public static DialogSelection getInstance(Builder mBuilder) {
        DialogSelection mDialogSelection = new DialogSelection();
        mDialogSelection.mBuilder = mBuilder;
        return mDialogSelection;
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
        View mView = inflater.inflate(R.layout.dialog_selection, null);
        ButterKnife.bind(this, mView);
        dialogBuilder.setView(mView);
        initData();
        return dialogBuilder.create();
    }

    private void initData() {
        if (!TextUtils.isEmpty(mBuilder.title))
            tvTitle.setText(mBuilder.title);
        if (!TextUtils.isEmpty(mBuilder.content))
            tvContent.setText(mBuilder.content);
        if (!TextUtils.isEmpty(mBuilder.txtAction1))
            tvAction1.setText(mBuilder.txtAction1);
        if (!TextUtils.isEmpty(mBuilder.txtAction2))
            tvAction2.setText(mBuilder.txtAction2);
    }

    @OnClick({R.id.tv_action_1, R.id.tv_action_2})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.tv_action_1:
                if (mBuilder.action1Listener != null)
                    mBuilder.action1Listener.onAction1Click(this);
                break;
            case R.id.tv_action_2:
                if (mBuilder.action2Listener != null)
                    mBuilder.action2Listener.onAction2Click(this);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.7f;
            windowParams.flags |= LayoutParams.FLAG_DIM_BEHIND;
            windowParams.flags |= LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
//            windowParams.windowAnimations = R.style.DialogAnimation;
            window.setAttributes(windowParams);
//            dialog.setCanceledOnTouchOutside(false);
//            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }


    public static final class Builder {
        private String title;
        private String content;
        private String txtAction1;
        private String txtAction2;
        private OnClickAction1Listener action1Listener;
        private OnClickAction2Listener action2Listener;

        public Builder() {
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setAction1(String txtAction1, OnClickAction1Listener action1Listener) {
            this.txtAction1 = txtAction1;
            this.action1Listener = action1Listener;
            return this;
        }

        public Builder setAction2(String txtAction2, OnClickAction2Listener action2Listener) {
            this.txtAction2 = txtAction2;
            this.action2Listener = action2Listener;
            return this;
        }

        public DialogSelection build() {
            return DialogSelection.getInstance(this);
        }
    }
}
