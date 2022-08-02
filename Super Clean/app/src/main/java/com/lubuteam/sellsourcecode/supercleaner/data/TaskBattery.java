package com.lubuteam.sellsourcecode.supercleaner.data;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;

import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;

public class TaskBattery extends AsyncTask<Void, Integer, Integer> {

    private Context mContext;
    private OnTaskBatteryListener mOnTaskBatteryListener;
    private String typeGetData;

    public TaskBattery(Context context, String typeGetData, OnTaskBatteryListener onTaskBatteryListener) {
        this.mContext = context;
        this.mOnTaskBatteryListener = onTaskBatteryListener;
        this.typeGetData = typeGetData;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        if (typeGetData.equals(BatteryManager.EXTRA_LEVEL))
            return getBatteryLevel(mContext);
        else if (typeGetData.equals(BatteryManager.EXTRA_TEMPERATURE))
            return getBatteryTemp(mContext);
        return 0;
    }

    @Override
    protected void onPostExecute(Integer value) {
        super.onPostExecute(value);
        if (null != mOnTaskBatteryListener) {
            mOnTaskBatteryListener.OnResult(value);
        }
    }

    public interface OnTaskBatteryListener {
        void OnResult(int pin);
    }

    public static int getBatteryLevel(Context context) {
        try {
            Intent intent = context.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            int intExtra = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            if (intExtra != -1) {
                if (scale != -1) {
                    return (int) ((((float) intExtra) / ((float) scale)) * 100.0f);
                }
            }
        } catch (Exception e) {
        }
        return 50;
    }

    public static int getBatteryTemp(Context context) {
        try {
            Intent intent = context.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            if (temperature != -1) {
                return Toolbox.getTempC(temperature);
            }
        } catch (Exception e) {
        }
        return 20;
    }
}
