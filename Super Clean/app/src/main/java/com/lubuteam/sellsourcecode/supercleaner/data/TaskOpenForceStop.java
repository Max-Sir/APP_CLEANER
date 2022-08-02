package com.lubuteam.sellsourcecode.supercleaner.data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.screen.main.MainActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;
import com.lubuteam.sellsourcecode.supercleaner.service.ForceStopAccessibility;
import com.lubuteam.sellsourcecode.supercleaner.window.DeepboostWindowmanager;

import java.util.List;

public class TaskOpenForceStop extends AsyncTask<Void, TaskInfo, Void> {

    private Context mContext;
    private List<TaskInfo> lstApp;

    public TaskOpenForceStop(Context mContext, List<TaskInfo> lstApp) {
        this.mContext = mContext;
        this.lstApp = lstApp;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (ForceStopAccessibility.getInstance() != null)
            ForceStopAccessibility.getInstance().showHideViewMark(lstApp);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        int i = 0;
        List<TaskInfo> lstKill = Toolbox.getLstTaskFillterIgnore(lstApp);
        while (i < lstKill.size()) {
            publishProgress(lstKill.get(i));
            i++;
            try {
                Thread.sleep(DeepboostWindowmanager.TIME_ONE_DEEP_CLEAN);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(TaskInfo... values) {
        super.onProgressUpdate(values);
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + values[0].getPackageName()));
        mContext.startActivity(intent);
        if (ForceStopAccessibility.getInstance() != null)
            ForceStopAccessibility.getInstance().startAutoClick();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mIntent = new Intent(mContext, MainActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mIntent.putExtra(Config.DATA_OPEN_RESULT, Config.FUNCTION.POWER_SAVING.id);
                mContext.startActivity(mIntent);
            }
        }, 300);
    }
}
