package com.lubuteam.sellsourcecode.supercleaner.widget;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AntivirusScanView extends LinearLayout {

    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.tv_appname)
    TextView tvAppName;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    @BindView(R.id.av_scan)
    LottieAnimationView animationScan;
    @BindView(R.id.av_progress)
    LottieAnimationView animationProgress;
//    @BindView(R.id.ll_virus)
//    FrameLayout llVirus;
//    @BindView(R.id.ll_dangerous)
//    FrameLayout llDangerous;

    public AntivirusScanView(Context context) {
        super(context);
        initView();
    }

    public AntivirusScanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.layout_animation_antivirus, this);
        ButterKnife.bind(this, mView);
        animationScan.setMaxFrame(140);
        animationScan.setMinFrame(20);
    }

    public void startAnimationScan() {
        animationScan.playAnimation();
        animationProgress.playAnimation();
    }

    public void stopAnimationScan() {
        animationScan.pauseAnimation();
        animationProgress.pauseAnimation();
        new Handler().postDelayed(() -> setVisibility(GONE), 1000);
    }

    public void setProgress(int progress) {
        tvProgress.setText(String.valueOf(progress));
        mProgressBar.setProgress(progress);
    }

    public void setContent(String content) {
        if (!TextUtils.isEmpty(content))
            tvAppName.setText(content);
    }

//    public void showBgVirus() {
//        if (llVirus.getAlpha() == 0.0f)
//            YoYo.with(Techniques.FadeIn).duration(1000).playOn(llVirus);
//    }
//
//    public void showBgDangerous() {
//        if (llDangerous.getAlpha() == 0.0f)
//            YoYo.with(Techniques.FadeIn).duration(1000).playOn(llDangerous);
//    }

}
