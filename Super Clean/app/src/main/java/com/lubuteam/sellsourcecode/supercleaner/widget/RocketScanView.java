package com.lubuteam.sellsourcecode.supercleaner.widget;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class RocketScanView extends RelativeLayout {

    @BindView(R.id.im_scanning)
    ImageView imScanning;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    @BindView(R.id.ll_progress)
    View llProgress;
    @BindView(R.id.present)
    TextView tvPresent;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_anmation_start)
    View llAnimationStart;
    @BindView(R.id.ll_anmation_done)
    LottieAnimationView llAnimationDone;
    @BindView(R.id.im_rocket)
    ImageView imRocket;
    @BindView(R.id.ll_main)
    View llMain;

    private Context mContext;


    public RocketScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public RocketScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_animation_rocket, this);
        ButterKnife.bind(this, mView);
        mProgressBar.setVisibility(GONE);
        tvPresent.setVisibility(GONE);
    }

    public void playAnimationStart() {
        llAnimationStart.setVisibility(VISIBLE);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setFillAfter(true);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        imScanning.startAnimation(rotate);
    }

    public void setContent(String txt) {
        tvContent.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(txt)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvContent.setText(Html.fromHtml(txt, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvContent.setText(Html.fromHtml(txt));
            }
        }
    }

    public void stopAnimationStart() {
        YoYo.with(Techniques.FadeOut).duration(1000).playOn(this);
        imScanning.clearAnimation();
        new Handler().postDelayed(() -> setVisibility(GONE), 1000);
    }

    public void playAnimationDone(AnimationListener mAnimationListener) {
        setVisibility(VISIBLE);
//        Toolbox.animationTransColor(getResources().getColor(R.color.color_ffa800), getResources().getColor(R.color.color_4254ff), 4000, llMain);
        Toolbox.animationTransColor(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark), 4000, llMain);
        llAnimationDone.setVisibility(VISIBLE);
        llAnimationStart.setVisibility(INVISIBLE);
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
