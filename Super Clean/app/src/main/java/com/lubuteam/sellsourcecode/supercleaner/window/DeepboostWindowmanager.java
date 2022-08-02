package com.lubuteam.sellsourcecode.supercleaner.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;
import com.lubuteam.sellsourcecode.supercleaner.widget.PowerScanView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.WINDOW_SERVICE;

public class DeepboostWindowmanager {

    public static long TIME_ONE_DEEP_CLEAN = 1000;

    @BindView(R.id.powerScanView)
    PowerScanView mPowerScanView;
    @BindView(R.id.ll_toolbar)
    LinearLayout llToolbar;


    private WindowManager mWindowManager;
    private Context mContext;
    private View mView;

    public DeepboostWindowmanager(Context mContext) {
        this.mContext = mContext;
        mWindowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        initView();
    }

    public WindowManager.LayoutParams setupLayout() {
        WindowManager.LayoutParams mLayoutParams;
        int flag = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        int type = Toolbox.isOreo() ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        int height = Toolbox.getHeightScreen(mContext) + Toolbox.getHeightStatusBar(mContext, true) + Toolbox.getHeightNavigationbar(mContext) + 100;
        mLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, height, type, flag, PixelFormat.TRANSLUCENT);
        mLayoutParams.gravity = Gravity.TOP;
        return mLayoutParams;
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mView = layoutInflater.inflate(R.layout.layout_window_deepboost, null);
        ButterKnife.bind(this, mView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Toolbox.getHeightStatusBar(mContext, true) > 0) {
            llToolbar.setPadding(0, Toolbox.getHeightStatusBar(mContext, true), 0, 0);
        }
    }

    public void startAnimation(List<TaskInfo> lstApp) {
        mWindowManager.addView(mView, setupLayout());
        mPowerScanView.setAlpha(1.0f);
        mPowerScanView.setVisibility(View.VISIBLE);
        mPowerScanView.playAnimationDone(lstApp, TIME_ONE_DEEP_CLEAN, () -> {
            YoYo.with(Techniques.FadeOut).duration(1000).playOn(mPowerScanView);
            new Handler().postDelayed(() -> {
                mPowerScanView.setVisibility(View.GONE);
                try {
                    mWindowManager.removeView(mView);
                } catch (Exception e) {

                }
            }, 1000);
        });
    }

}
