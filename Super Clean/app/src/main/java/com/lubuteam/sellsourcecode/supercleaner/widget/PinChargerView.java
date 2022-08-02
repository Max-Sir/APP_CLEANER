package com.lubuteam.sellsourcecode.supercleaner.widget;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.lubuteam.sellsourcecode.supercleaner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PinChargerView extends RelativeLayout {

    @BindView(R.id.ll_anmation_start_2)
    LottieAnimationView llAnimationStart2;
    @BindView(R.id.ll_anmation_start_1)
    LottieAnimationView llAnimationStart1;
    @BindView(R.id.ll_anmation_done)
    LottieAnimationView llAnimationDone;
    @BindView(R.id.tv_content)
    TextView tvContent;

    public PinChargerView(Context context) {
        super(context);
        initView();
    }

    public PinChargerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.layout_animation_pin_recharger, this);
        ButterKnife.bind(this, mView);
    }

    public void playAnimationDone() {
        llAnimationStart2.pauseAnimation();
        llAnimationStart2.setVisibility(INVISIBLE);
        llAnimationStart1.pauseAnimation();
        llAnimationStart1.setVisibility(INVISIBLE);
        llAnimationDone.playAnimation();
    }

    public void setContent(String txt) {
        if (!TextUtils.isEmpty(txt)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvContent.setText(Html.fromHtml(txt, Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvContent.setText(Html.fromHtml(txt));
            }
        }
    }
}
