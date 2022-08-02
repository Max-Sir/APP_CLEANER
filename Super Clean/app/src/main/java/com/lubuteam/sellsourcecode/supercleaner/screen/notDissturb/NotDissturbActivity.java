package com.lubuteam.sellsourcecode.supercleaner.screen.notDissturb;

import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotDissturbActivity extends BaseActivity {

    @BindView(R.id.im_back_toolbar)
    ImageView imBack;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;

    @BindView(R.id.rl_DND_start)
    LinearLayout rlDNDStart;
    @BindView(R.id.rl_DND_stop)
    LinearLayout rlDNDStop;
    @BindView(R.id.tv_DND_start)
    TextView tvDNDStart;
    @BindView(R.id.tv_DND_start_time)
    TextView tvDNDStartSecond;
    @BindView(R.id.tv_DND_stop)
    TextView tvDNDStop;
    @BindView(R.id.tv_DND_stop_time)
    TextView tvDNDStopSecond;
    @BindView(R.id.tvEnable)
    TextView tvEnable;
    @BindView(R.id.swDND)
    SwitchCompat swDND;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_dissturb);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        imBack.setVisibility(View.VISIBLE);
//        imBack.setColorFilter(getResources().getColor(R.color.color_222222), PorterDuff.Mode.SRC_IN);
        tvToolbar.setText(getString(R.string.not_distub));
//        tvToolbar.setTextColor(getResources().getColor(R.color.color_222222));
    }

    private void initData() {
        intTimeOn(PreferenceUtils.getDNDStart());
        intTimeOff(PreferenceUtils.getDNDEnd());
        setColorText(PreferenceUtils.isDND());
        if (PreferenceUtils.isDND()) {
            tvEnable.setText(getString(R.string.on));
        } else {
            tvEnable.setText(getString(R.string.off));
        }
    }

    public void intTimeOn(int time) {
        int dNDStartTime = time;
        int i = dNDStartTime / 100;
        dNDStartTime %= 100;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%02d", Integer.valueOf(dNDStartTime)));
        tvDNDStartSecond.setText(stringBuilder.toString());
    }

    public void intTimeOff(int time) {
        int dNDEndTime = time;
        int i = dNDEndTime / 100;
        dNDEndTime %= 100;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%02d", Integer.valueOf(dNDEndTime)));
        tvDNDStopSecond.setText(stringBuilder.toString());
    }

    private void setColorText(boolean isChecked) {
        if (isChecked) {
            tvEnable.setTextColor(ContextCompat.getColor(this, R.color.color_a5a5ff));
            swDND.setChecked(isChecked);
            rlDNDStart.setEnabled(true);
            tvDNDStartSecond.setTextColor(ContextCompat.getColor(this, R.color.color_a5a5ff));
            tvDNDStopSecond.setTextColor(ContextCompat.getColor(this, R.color.color_a5a5ff));
            tvDNDStart.setTextColor(ContextCompat.getColor(this, R.color.color_726882));
            tvDNDStop.setTextColor(ContextCompat.getColor(this, R.color.color_726882));

        } else {
            tvEnable.setTextColor(ContextCompat.getColor(this, R.color.color_9a9a9a));
            swDND.setChecked(isChecked);
            rlDNDStart.setEnabled(false);
            tvDNDStartSecond.setTextColor(ContextCompat.getColor(this, R.color.color_9a9a9a));
            tvDNDStopSecond.setTextColor(ContextCompat.getColor(this, R.color.color_9a9a9a));
            tvDNDStart.setTextColor(ContextCompat.getColor(this, R.color.color_9a9a9a));
            tvDNDStop.setTextColor(ContextCompat.getColor(this, R.color.color_9a9a9a));
        }
    }

    @OnClick({R.id.rl_DND_start, R.id.rl_DND_stop, R.id.rlDND})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.rlDND:
                if (PreferenceUtils.isDND()) {
                    setColorText(false);
                    tvEnable.setText(getString(R.string.off));
                    PreferenceUtils.setDND(false);
                } else {
                    tvEnable.setText(getString(R.string.on));
                    setColorText(true);
                    PreferenceUtils.setDND(true);
                }
                break;
            case R.id.rl_DND_start:
                int timeStart = PreferenceUtils.getDNDStart();
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i2) -> {
                    PreferenceUtils.setDNDStart((i * 100) + i2);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
                    stringBuilder.append(":");
                    stringBuilder.append(String.format("%02d", Integer.valueOf(i2)));
                    tvDNDStartSecond.setText(stringBuilder.toString());

                }, timeStart / 100, timeStart % 100, true);
                timePickerDialog.setTitle(this.getString(R.string.start_at));
                timePickerDialog.show();

                break;
            case R.id.rl_DND_stop:
                int timeStop = PreferenceUtils.getDNDEnd();
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(this, (timePicker, i, i2) -> {
                    PreferenceUtils.setDNDEnd((i * 100) + i2);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
                    stringBuilder.append(":");
                    stringBuilder.append(String.format("%02d", Integer.valueOf(i2)));
                    tvDNDStopSecond.setText(stringBuilder.toString());

                }, timeStop / 100, timeStop % 100, true);
                timePickerDialog2.setTitle(this.getString(R.string.stop_at));
                timePickerDialog2.show();
                break;
        }
    }
}
