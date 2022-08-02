package com.lubuteam.sellsourcecode.supercleaner.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.listener.animation.AnimationListener;
import com.lubuteam.sellsourcecode.supercleaner.model.ChildItem;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CleanJunkFileView extends RelativeLayout {
    @BindView(R.id.av_clean_file)
    LottieAnimationView avCleanJunk;
    @BindView(R.id.ll_main)
    View llMain;
    @BindView(R.id.im_iconApp)
    RoundedImageView imIconApp;

    public CleanJunkFileView(Context context) {
        super(context);
        initView();
    }

    public CleanJunkFileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.layout_animation_junk_clean, this);
        ButterKnife.bind(this, mView);
    }

    public void startAnimationClean(List<ChildItem> lstItem, int timeOneItem, AnimationListener mAnimationListener) {
        setVisibility(VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(this);
        avCleanJunk.setMaxFrame(40);
        avCleanJunk.setMinFrame(30);
        avCleanJunk.playAnimation();
        startProgress(lstItem, timeOneItem, mAnimationListener);
    }

    public void startProgress(List<ChildItem> lstItem, long timeOneItem, AnimationListener mAnimationListener) {
        final ValueAnimator ofInt = ValueAnimator.ofInt(0, lstItem.size() - 1);
        ofInt.setDuration(lstItem.size() * timeOneItem);
        ofInt.setInterpolator(new AccelerateDecelerateInterpolator());
        ofInt.addUpdateListener(animation -> {
            int position = Integer.parseInt(animation.getAnimatedValue().toString());
            ChildItem mChildItem = lstItem.get(position);
            if (mChildItem.getType() == ChildItem.TYPE_APKS) {
                imIconApp.setImageResource(R.drawable.ic_android);
            } else if (mChildItem.getType() == ChildItem.TYPE_CACHE) {
                imIconApp.setImageDrawable(mChildItem.getApplicationIcon());
            } else if (mChildItem.getType() == ChildItem.TYPE_DOWNLOAD_FILE) {
                imIconApp.setImageResource(R.drawable.ic_file_download);
            } else if (mChildItem.getType() == ChildItem.TYPE_LARGE_FILES) {
                imIconApp.setImageResource(R.drawable.ic_description);
            }
            if (position == lstItem.size() - 1) {
                YoYo.with(Techniques.FadeOut).duration(1000).playOn(this);
                new Handler().postDelayed(() -> {
                    mAnimationListener.onStop();
                }, 500);
                ofInt.cancel();
            }
        });
        ofInt.start();
    }

}
