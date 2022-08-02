package com.lubuteam.sellsourcecode.supercleaner.data;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;

import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;

public class TaskChargeDetail extends AsyncTask<Void, Integer, Void> {

    private OnTaskBoostListener mOnTaskBoostListener;
    private Context mContext;

    public TaskChargeDetail(Context context, OnTaskBoostListener onTaskBoostListener) {
        mOnTaskBoostListener = onTaskBoostListener;
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int j = 0; j < 100; j++) {
            publishProgress(j);
            try {
                if (j <= 40) {
                    Thread.sleep(50);
                } else {
                    Thread.sleep(30);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (values[0] == 15) {
            if (!PreferenceUtils.getValueRecharger(PreferenceUtils.RECHARGE_WIFI)) {
                WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    wifiManager.setWifiEnabled(false);
                }

            }
            return;
        }
        if (values[0] == 30) {
            if (PreferenceUtils.getValueRecharger(PreferenceUtils.RECHARGE_BLUETOOTH)) {
                try {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter != null)
                        bluetoothAdapter.disable();
                } catch (SecurityException e) {

                }
            }

            return;
        }
        if (values[0] == 60) {
            if (!PreferenceUtils.getValueRecharger(PreferenceUtils.RECHARGE_SYNC)) {
                ContentResolver.setMasterSyncAutomatically(false);
            }
            return;
        }
        if (values[0] == 70) {
            if (!PreferenceUtils.getValueRecharger(PreferenceUtils.RECHARGE_BRIGHTNESS)) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (Settings.System.canWrite(mContext)) {
                        Settings.System.putInt(mContext.getContentResolver(),
                                Settings.System.SCREEN_BRIGHTNESS_MODE,
                                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                    }
                } else {
                    Settings.System.putInt(mContext.getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                }
            }
            return;
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        if (null != mOnTaskBoostListener) {
            mOnTaskBoostListener.OnResult();
        }
    }

    public interface OnTaskBoostListener {
        void OnResult();
    }


}