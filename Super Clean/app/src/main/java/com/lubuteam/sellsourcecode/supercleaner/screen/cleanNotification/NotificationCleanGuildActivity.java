package com.lubuteam.sellsourcecode.supercleaner.screen.cleanNotification;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.BaseApp;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationCleanGuildActivity extends BaseActivity {

    private List<ObjectAnimator> listAnimation = new ArrayList();
    @BindView(R.id.iv_icon_1)
    View mIvIcon1;
    @BindView(R.id.iv_icon_2)
    View mIvIcon2;
    @BindView(R.id.iv_icon_3)
    View mIvIcon3;
    @BindView(R.id.iv_item_1)
    View mIvItem1;
    @BindView(R.id.iv_item_2)
    View mIvItem2;
    @BindView(R.id.iv_item_3)
    View mIvItem3;
    @BindView(R.id.iv_start_0_0)
    View mIvStart00;
    @BindView(R.id.iv_start_0_1)
    View mIvStart01;
    @BindView(R.id.iv_start_0_2)
    View mIvStart02;
    @BindView(R.id.iv_start_1_0)
    View mIvStart10;
    @BindView(R.id.iv_start_1_1)
    View mIvStart11;
    @BindView(R.id.iv_start_1_2)
    View mIvStart12;
    @BindView(R.id.iv_start_1_3)
    View mIvStart13;
    @BindView(R.id.layout_icon)
    View mLayoutIcon;
    @BindView(R.id.layout_item_0)
    View mLayoutItem0;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_label)
    TextView mTvLabel;
    @BindView(R.id.im_back_toolbar)
    ImageView imBackToolbar;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_notification_guild);
        ButterKnife.bind(this);
        imBackToolbar.setVisibility(View.VISIBLE);
        tvToolbar.setText(getString(R.string.notification_manager));
        this.mLayoutItem0.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                BaseApp.a(() -> {
                    if (!NotificationCleanGuildActivity.this.isFinishing()) {
                        NotificationCleanGuildActivity.this.u();
                    }
                }, 500);
                mLayoutItem0.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void u() {
        this.mLayoutItem0.setPivotX((float) (this.mLayoutItem0.getWidth() / 2));
        this.mLayoutItem0.setPivotY((float) this.mLayoutItem0.getHeight());
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mLayoutItem0, View.SCALE_X, 1.0f, 1.34f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mLayoutItem0, View.SCALE_Y, 1.0f, 1.34f);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.mTvLabel, View.ALPHA, 1.0f, Float.intBitsToFloat(1));
        ofFloat2.setInterpolator(new LinearInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                if (!NotificationCleanGuildActivity.this.isFinishing() && mIvItem1 != null) {
                    NotificationCleanGuildActivity.this.a(mIvItem1, 0);
                }
            }
        });
        animatorSet.play(ofFloat).with(ofFloat2).with(ofFloat3);
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    public void a(View view, final int i) {
        if (view != null) {
            view.setPivotX((float) (view.getWidth() / 2));
            view.setPivotY(Float.intBitsToFloat(1));
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, Float.intBitsToFloat(1));
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, Float.intBitsToFloat(1));
            AnimatorSet.Builder with = animatorSet.play(ofFloat).with(ofFloat2).with(ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, Float.intBitsToFloat(1)));
            int height = this.mIvItem1.getHeight() + Toolbox.getDimension(10.0f);
            if (i == 0) {
                with.with(ObjectAnimator.ofFloat(this.mIvItem2, View.TRANSLATION_Y, 0.0f, (float) (-height)));
            }
            if (i == 0 || i == 1) {
                int i2 = -height;
                with.with(ObjectAnimator.ofFloat(this.mIvItem3, View.TRANSLATION_Y, (float) (i2 * i), (float) (i2 * (i + 1))));
            }
            animatorSet.addListener(new Animator.AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    if (!NotificationCleanGuildActivity.this.isFinishing()) {
                        NotificationCleanGuildActivity.this.d(i);
                        int tmp = i + 1;
                        if (tmp > 2) {
                            BaseApp.a(() -> NotificationCleanGuildActivity.this.v(), 400);
                        } else {
                            BaseApp.a(() -> {
                                if (!NotificationCleanGuildActivity.this.isFinishing()) {
                                    NotificationCleanGuildActivity.this.a(tmp == 1 ? mIvItem2 : mIvItem3, tmp);
                                }
                            }, 300);
                        }
                    }
                }
            });
            animatorSet.setDuration(650);
            animatorSet.setInterpolator(new LinearInterpolator());
            animatorSet.start();
        }
    }

    public void d(int i) {
        if (this.mLayoutIcon != null && this.mTvLabel != null && this.mTvCount != null) {
            this.mLayoutIcon.setVisibility(View.VISIBLE);
            this.mTvLabel.setAlpha(1.0f);
            this.mTvLabel.setText(getString(R.string.Notificationbar_Spamnotification));
            this.mTvCount.setText(String.valueOf(i + 1));
            View view = i == 0 ? this.mIvIcon1 : i == 1 ? this.mIvIcon2 : this.mIvIcon3;
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.SCALE_X, Float.intBitsToFloat(1), 1.0f);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, View.SCALE_Y, Float.intBitsToFloat(1), 1.0f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(ofFloat).with(ofFloat2);
            animatorSet.setDuration(300);
            animatorSet.setInterpolator(new LinearInterpolator());
            animatorSet.start();
        }
    }

    public void v() {
        if (this.mLayoutItem0 != null) {
            this.mLayoutItem0.setPivotX((float) (this.mLayoutItem0.getWidth() / 2));
            this.mLayoutItem0.setPivotY((float) this.mLayoutItem0.getHeight());
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mLayoutItem0, View.SCALE_X, 1.34f, 1.0f);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mLayoutItem0, View.SCALE_Y, 1.34f, 1.0f);
            ofFloat2.setInterpolator(new LinearInterpolator());
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.addListener(new Animator.AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    if (!NotificationCleanGuildActivity.this.isFinishing()) {
                        BaseApp.a(new Runnable() {
                            public final void run() {
                                aaa();
                            }
                        }, 200);
                    }
                }

                public void aaa() {
                    if (!NotificationCleanGuildActivity.this.isFinishing()) {
                        NotificationCleanGuildActivity.this.A();
                    }
                }
            });
            animatorSet.play(ofFloat).with(ofFloat2);
            animatorSet.setDuration(300);
            animatorSet.setInterpolator(new LinearInterpolator());
            animatorSet.start();
        }
    }

    public void A() {
        animationModel(this.mIvStart00, 0).start();
        animationModel(this.mIvStart01, 0).start();
        animationModel(this.mIvStart02, 0).start();
        animationModel(this.mIvStart10, 800).start();
        animationModel(this.mIvStart11, 800).start();
        animationModel(this.mIvStart12, 800).start();
        animationModel(this.mIvStart13, 800).start();
    }

    private ObjectAnimator animationModel(final View view, long j) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat(View.SCALE_X, Float.intBitsToFloat(1), 1.0f, Float.intBitsToFloat(1)),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, Float.intBitsToFloat(1), 1.0f, Float.intBitsToFloat(1)),
                PropertyValuesHolder.ofFloat(View.ALPHA, Float.intBitsToFloat(1), 1.0f, Float.intBitsToFloat(1)));
        ofPropertyValuesHolder.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }
        });
        ofPropertyValuesHolder.setRepeatCount(-1);
        ofPropertyValuesHolder.setStartDelay(j);
        ofPropertyValuesHolder.setInterpolator(new LinearInterpolator());
        ofPropertyValuesHolder.setDuration(1600);
        listAnimation.add(ofPropertyValuesHolder);
        return ofPropertyValuesHolder;
    }


    public void onStop() {
        if (isFinishing()) {
            for (ObjectAnimator cancel : listAnimation) {
                cancel.cancel();
            }
        }
        super.onStop();
    }

    @OnClick(R.id.btn_open)
    public void open() {
        try {
            askPermissionNotificaitonSetting(() -> {
                startActivity(new Intent(NotificationCleanGuildActivity.this, NotificationCleanSettingActivity.class));
                finish();
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
