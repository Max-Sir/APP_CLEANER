package com.lubuteam.sellsourcecode.supercleaner.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.listener.animation.AnimationListener;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.a.a.c;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PowerScanView extends RelativeLayout {

    @BindView(R.id.ll_anmation_scan)
    LottieAnimationView llAnimationScan;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.layout_anim_container)
    RelativeLayout layoutAnimContainer;
    @BindView(R.id.tv_content_done)
    TextView tvContentDone;
    @BindView(R.id.im_iconApp)
    RoundedImageView imIconApp;
    @BindView(R.id.ll_main)
    View llMain;

    private Context mContext;
    private PackageManager packageManager;

    private Integer[] lstReource = new Integer[]{
            R.drawable.boost_profile_0, R.drawable.boost_profile_1, R.drawable.boost_profile_2, R.drawable.boost_profile_3, R.drawable.boost_profile_4
            , R.drawable.boost_profile_5, R.drawable.boost_profile_6, R.drawable.boost_profile_7
    };

    public PowerScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public PowerScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_anmation_power, this);
        ButterKnife.bind(this, mView);
        packageManager = mContext.getPackageManager();
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

    public void playAnimationDone(List<TaskInfo> lstApp, long timeCleanOneApp, AnimationListener mAnimationListener) {
        setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        llAnimationScan.setVisibility(INVISIBLE);
        tvContent.setVisibility(INVISIBLE);
        List arrayList = new ArrayList();
        for (int i = 0; i < 8; i++) {
            int identifier = lstReource[i];
            ImageView appCompatImageView = new ImageView(mContext);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(13, -1);
            appCompatImageView.setLayoutParams(layoutParams);
            appCompatImageView.setImageResource(identifier);
            appCompatImageView.setPadding(0, Toolbox.getDesity(mContext, 30), 0, Toolbox.getDesity(mContext, 30));
            appCompatImageView.setAlpha(0.5f);
            arrayList.add(appCompatImageView);
            this.layoutAnimContainer.addView(appCompatImageView);
            c.a(appCompatImageView).a(10000).d(0.5f, 1.0f).g(0.5f, 1.1f, 1.0f).b((long) (i * 300)).e();
        }
        c.a(imIconApp).a(10000).g(0.9f, 1.1f, 1.0f).b((300)).e();
        c.a((View) arrayList.get(0)).j(0.0f, 1440.0f).a(20000).a(new AccelerateDecelerateInterpolator()).a(-1).b(2).e();
        c.a((View) arrayList.get(1)).j(0.0f, 360.0f).a(80000).a(new LinearInterpolator()).a(-1).b(2).e();
        c.a((View) arrayList.get(2)).j(0.0f, -360.0f).a(80000).a(new LinearInterpolator()).a(-1).b(2).e();
        c.a((View) arrayList.get(3)).j(0.0f, 720.0f).a(360000).a(new AccelerateDecelerateInterpolator()).a(-1).b(-1).e();
        c.a((View) arrayList.get(4)).j(0.0f, -720.0f).a(360000).a(new AccelerateDecelerateInterpolator()).a(-1).b(-1).e();
        c.a((View) arrayList.get(5)).j(0.0f, 1440.0f).a(20000).a(new AccelerateDecelerateInterpolator()).a(-1).b(2).e();
        c.a((View) arrayList.get(6)).j(0.0f, -2160.0f).a(20000).a(new AccelerateDecelerateInterpolator()).a(-1).b(2).e();
        c.a((View) arrayList.get(7)).k().a(-1).b(2).e();
        c.a(this.layoutAnimContainer).g(1.0f, 1.1f, 1.0f).a(-1).b(-1).e();
        startProgress(lstApp, timeCleanOneApp, mAnimationListener);
    }

    public void startProgress(List<TaskInfo> lstApp, long timeCleanOneApp, AnimationListener mAnimationListener) {
        final ValueAnimator ofInt = ValueAnimator.ofInt(0, lstApp.size() - 1);
        ofInt.setDuration(lstApp.size() * timeCleanOneApp);
//        Toolbox.animationTransColor(getResources().getColor(R.color.color_ffa800), getResources().getColor(R.color.color_4254ff), lstApp.size() * 300, llMain);
        Toolbox.animationTransColor(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark), lstApp.size() * 300, llMain);
        ofInt.setInterpolator(new AccelerateDecelerateInterpolator());
        ofInt.addUpdateListener(animation -> {
            int position = Integer.parseInt(animation.getAnimatedValue().toString());
            setContentDone(mContext.getString(R.string.hibernation) + lstApp.get(position).getTitle());
            imIconApp.setImageDrawable(lstApp.get(position).getAppinfo().loadIcon(packageManager));
            if (position == lstApp.size() - 1) {
                new Handler().postDelayed(() -> mAnimationListener.onStop(), 1000);
            }
        });
        ofInt.start();
    }

    public void setContentDone(String txt) {
        tvContentDone.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(txt)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvContentDone.setText(Html.fromHtml(txt, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvContentDone.setText(Html.fromHtml(txt));
            }
        }
    }

}
