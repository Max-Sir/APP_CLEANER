package com.lubuteam.sellsourcecode.supercleaner.widget;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.listener.animation.AnimationListener;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CpuScanView extends RelativeLayout {

    @BindView(R.id.ll_anmation_scan)
    LottieAnimationView llAnimationScan;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_anmation_done)
    LottieAnimationView llAnimationDone;
    @BindView(R.id.ll_main)
    View llMain;

    private Context mContext;

    public CpuScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public CpuScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_anmation_cpu, this);
        ButterKnife.bind(this, mView);
    }

    public void playAnimationStart() {
        llAnimationScan.setVisibility(VISIBLE);
        llAnimationScan.playAnimation();
        tvContent.setVisibility(VISIBLE);
        YoYo.with(Techniques.Flash).duration(2000).repeat(1000).playOn(tvContent);
    }

    public void stopAnimationStart() {
        YoYo.with(Techniques.FadeOut).duration(1000).playOn(this);
        llAnimationScan.pauseAnimation();
        new Handler().postDelayed(() -> setVisibility(GONE), 1000);
    }

    public void playAnimationDone(AnimationListener mAnimationListener) {
        setVisibility(VISIBLE);
//        Toolbox.animationTransColor(getResources().getColor(R.color.color_ffa800), getResources().getColor(R.color.color_4254ff), 3000, llMain);
        Toolbox.animationTransColor(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark), 3000, llMain);
        llAnimationDone.setVisibility(VISIBLE);
        llAnimationScan.setVisibility(INVISIBLE);
        tvContent.setVisibility(INVISIBLE);
        llAnimationDone.playAnimation();
        llAnimationDone.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationListener.onStop();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
