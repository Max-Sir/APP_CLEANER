package com.lubuteam.sellsourcecode.supercleaner.screen.main.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.control.AdmobHelp;
import com.ads.control.funtion.UtilsApp;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseFragment;
import com.lubuteam.sellsourcecode.supercleaner.screen.setting.SettingActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentPersional extends BaseFragment {

    @BindView(R.id.tv_header_persional)
    TextView tvPersionalHeader;

    public static FragmentPersional getInstance() {
        FragmentPersional mFragmentPersional = new FragmentPersional();
        Bundle mBundle = new Bundle();
        mFragmentPersional.setArguments(mBundle);
        return mFragmentPersional;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_persional_new, container, false);
        ButterKnife.bind(this, mView);
        initData();
        return mView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AdmobHelp.getInstance().loadBannerFragment(getActivity(), view);
    }
    private void initData() {
        int day = (int) ((System.currentTimeMillis() - PreferenceUtils.getTimeInstallApp()) / (24 * 60 * 60 * 1000));
        tvPersionalHeader.setText(getString(R.string.has_protected_your_phone, getString(R.string.app_name), String.valueOf(day == 0 ? 1 : day)));
    }

    @OnClick({R.id.ll_settings, R.id.ll_feedback, R.id.ll_game, R.id.ll_like_fb, R.id.ll_share, R.id.ll_upgrade})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.ll_settings:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.ll_feedback:
                UtilsApp.SendFeedBack(getActivity(),getString(R.string.email_feedback),getString(R.string.Title_email));
                break;
            case R.id.ll_game:
                UtilsApp.OpenBrower(getActivity(),getResources().getString(R.string.link_store_more_app));
                break;
            case R.id.ll_like_fb:
                UtilsApp.OpenBrower(getActivity(),getResources().getString(R.string.link_fb));
                break;
            case R.id.ll_share:
                UtilsApp.shareApp(getActivity());
                break;
            case R.id.ll_upgrade:
                UtilsApp.RateApp(getActivity());
                break;
        }
    }
}
