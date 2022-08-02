package com.lubuteam.sellsourcecode.supercleaner.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;
import com.lubuteam.sellsourcecode.supercleaner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HorizontalProgressView extends LinearLayout {

    private static final String TITLE_FORMAT = "%s%%";

    private int title;
    private int color;
    private int max;
    private int value;

    @BindView(R.id.tv_title)
    TextView titleView;
    @BindView(R.id.tv_value)
    TextView valueView;
    @BindView(R.id.rc_progress)
    RoundCornerProgressBar progressView;

    public HorizontalProgressView(Context context) {
        super(context);
        initView();
    }

    public HorizontalProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        obtainAttributes(attrs);
        initView();
    }

    public HorizontalProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttributes(attrs);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_horizontal_progress, this);
        ButterKnife.bind(this, view);

        progressView.setOnProgressChangedListener(new BaseRoundCornerProgressBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(View view, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress) {
                valueView.setText(String.format(TITLE_FORMAT, (int) progress));
            }
        });

        fillComponent();
    }

    private void fillComponent() {
        titleView.setText(title);
        titleView.setTextColor(color);
        valueView.setText(String.format(TITLE_FORMAT, value));
        progressView.setMax(max);
        progressView.setProgress(value);
        progressView.setProgressColor(color);
    }

    public void setProgress(int progress) {
        progressView.setProgress(progress);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final String STATE = "state";
    private static final String TITLE = "title";
    private static final String COLOR = "color";
    private static final String MAX = "max";
    private static final String VALUE = "value";

    @Override
    public Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(STATE, super.onSaveInstanceState());
        bundle.putInt(TITLE, title);
        bundle.putInt(COLOR, color);
        bundle.putInt(MAX, max);
        bundle.putInt(VALUE, value);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            title = bundle.getInt(TITLE);
            color = bundle.getInt(COLOR);
            max = bundle.getInt(MAX);
            value = bundle.getInt(VALUE);
            fillComponent();
            super.onRestoreInstanceState(bundle.getParcelable(STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    protected void obtainAttributes(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressView);
        title = ta.getResourceId(R.styleable.HorizontalProgressView_progressTitle, R.string.HorizontalProgressView_title);
        color = ta.getColor(R.styleable.HorizontalProgressView_progressLineColor, 0);
        max = ta.getInt(R.styleable.HorizontalProgressView_progressMax, 0);
        value = ta.getInt(R.styleable.HorizontalProgressView_progressValue, 0);
        ta.recycle();
    }

}