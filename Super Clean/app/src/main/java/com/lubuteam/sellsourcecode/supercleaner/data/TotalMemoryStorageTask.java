package com.lubuteam.sellsourcecode.supercleaner.data;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;

public class TotalMemoryStorageTask extends AsyncTask<Void, Integer, Boolean> {

    private long totalMemory;
    private long useMemory;

    private DataCallBack mDataCallBack;

    public interface DataCallBack {
        void getDataMemory(long useMemory, long totalMemory);
    }


    public TotalMemoryStorageTask(DataCallBack mDataCallBack) {
        this.mDataCallBack = mDataCallBack;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            totalMemory = getTotalMemoryStorage();
            long availableMemory = getMemoryStorageAvailable();
            useMemory = totalMemory - availableMemory;
            int percentMemory = (int) (((double) useMemory / (double) totalMemory) * 100);
            if (percentMemory > 100) {
                percentMemory = percentMemory % 100;
            }
            for (int i = 0; i <= percentMemory; i++) {
                publishProgress(i);
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (mDataCallBack != null)
            mDataCallBack.getDataMemory(useMemory, totalMemory);
    }

    private long getTotalMemoryStorage() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (long) statFs.getBlockSize() * (long) statFs.getBlockCount();
    }

    private long getMemoryStorageAvailable() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
    }
}
