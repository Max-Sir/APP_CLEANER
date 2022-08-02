package com.lubuteam.sellsourcecode.supercleaner.data;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;

import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskGetListApp extends AsyncTask<Void, Void, List<TaskInfo>> {

    private Context mContext;
    private OnResult mOnResult;

    public interface OnResult {
        void getListAp(List<TaskInfo> lstApp);
    }

    public TaskGetListApp(Context mContext, OnResult mOnResult) {
        this.mContext = mContext;
        this.mOnResult = mOnResult;
    }

    @Override
    protected List<TaskInfo> doInBackground(Void... voids) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = mContext.getPackageManager().queryIntentActivities(mainIntent, 0);

        List<TaskInfo> lstApp = new ArrayList<>();
        for (ResolveInfo mResolveInfo : pkgAppsList) {
            try {
                ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mResolveInfo.activityInfo.packageName, 0);
                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    lstApp.add(new TaskInfo(mContext, appInfo));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lstApp, (o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
        return lstApp;
    }

    @Override
    protected void onPostExecute(List<TaskInfo> resolveInfos) {
        super.onPostExecute(resolveInfos);
        if (mOnResult != null)
            mOnResult.getListAp(resolveInfos);
    }
}
