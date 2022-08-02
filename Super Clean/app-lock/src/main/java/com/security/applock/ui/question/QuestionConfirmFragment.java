package com.security.applock.ui.question;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;

import com.security.applock.App;
import com.security.applock.R;
import com.security.applock.databinding.FragmentQuestionBinding;
import com.security.applock.service.AntiTheftService;
import com.security.applock.ui.BaseFragment;
import com.security.applock.ui.main.MainActivity;
import com.security.applock.ui.password.PasswordActivity;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;

public class QuestionConfirmFragment extends BaseFragment<FragmentQuestionBinding> {

    private static final int CHANGE_PASSWORD_REQUEST = 3;
    private int action = -1;
    private boolean hasSaveQuestion;
    private String codeConfirm;

    @Override
    protected void initView() {
        binding.btnOk.setText(getResources().getString(R.string.action_confirm));

        if (getArguments() != null) {
            action = getArguments().getInt(Config.KeyBundle.KEY_CHANGE_QUESTION, -1);
            codeConfirm = getArguments().getString(Config.KeyBundle.KEY_PATTERN_CODE);
        }

        if (action == Config.DataBundle.DATA_FIRST_SETUP_QUESTION) {
            hasSaveQuestion = true;
            binding.btnOk.setText(getString(R.string.save));
        }
    }

    @Override
    protected void initControl() {
//        binding.imMenu.setOnClickListener(view -> {
        binding.llTop.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(getContext(), binding.tvQuestion);
            for (int i = 0; i < Config.LST_QUESTION.length; i++) {
                popup.getMenu().add(0, i, 0, Config.LST_QUESTION[i]);
            }
            popup.setOnMenuItemClickListener(item -> {
                binding.tvQuestion.setText(item.getTitle());
                return true;
            });
            popup.show();
        });

        binding.btnOk.setOnClickListener(view -> checkQuestionAnser());
    }

    private void checkQuestionAnser() {
        /**kiểm tra câu trả lời ko được trống*/
        if (TextUtils.isEmpty(binding.edtAnswer.getText())) {
            toast(getResources().getString(R.string.message_anser_empty));
            return;
        }

        if (hasSaveQuestion) {
            /**Lưu câu hởi mới và thoát*/
            PreferencesHelper.setQuestionAnser(binding.tvQuestion.getText().toString()
                    , binding.edtAnswer.getText().toString());
            toast(getString(R.string.message_change_question_anser_success));
            if (!TextUtils.isEmpty(codeConfirm)) {
                PreferencesHelper.setPatternCode(codeConfirm);
                App.getInstace().setForceLockScreen(true);
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            } else {
                navigateUp();
            }
            return;
        }

        /**kiểm tra câu hỏi và câu trả lời có đúng*/
        if (!PreferencesHelper.checkQuestionAnser(binding.tvQuestion.getText().toString(), binding.edtAnswer.getText().toString())) {
            toast(getResources().getString(R.string.message_error_question_anser));
            return;
        }

        if (action == Config.DataBundle.DATA_CHANGE_QUESTION) {
            resetView();
            binding.btnOk.setText(getString(R.string.save));
            hasSaveQuestion = true;
            toast(getString(R.string.message_enter_new_question_anser));
        } else {
            gotosetupPassword();
            if (PreferencesHelper.getInt(PreferencesHelper.DETECTION_TYPE, 0) != Config.DETECTION_NONE) {
                PreferencesHelper.putInt(PreferencesHelper.DETECTION_TYPE, Config.DETECTION_NONE);
                Intent intent = new Intent(getActivity(), AntiTheftService.class);
                intent.setAction(Config.ActionIntent.ACTION_STOP_ANTI_THEFT);
                getActivity().startService(intent);
            }
        }
    }

    private void resetView() {
        binding.edtAnswer.setText("");
        binding.tvQuestion.setText(Config.LST_QUESTION[0]);
    }

    private void gotosetupPassword() {
        App.getInstace().setForceLockScreen(true);
        Intent intent = new Intent(getActivity(), PasswordActivity.class);
        if (PreferencesHelper.isPatternCode())
            intent.setAction(Config.ActionIntent.ACTION_CHANGE_PATTERN_CODE);
        else
            intent.setAction(Config.ActionIntent.ACTION_CHANGE_PIN_CODE);
        startActivityForResult(intent, CHANGE_PASSWORD_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHANGE_PASSWORD_REQUEST:
                    navigateUp();
                    break;
            }
        }
    }

    @Override
    protected FragmentQuestionBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentQuestionBinding.inflate(inflater, container, false);
    }

    @Override
    protected int getTitleFragment() {
        return R.string.sercurity_question;
    }
}

