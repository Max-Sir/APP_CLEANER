package com.lubuteam.sellsourcecode.supercleaner.data;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.SQLException;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.lubuteam.sellsourcecode.supercleaner.database.DataHelper;
import com.lubuteam.sellsourcecode.supercleaner.database.FSEnvi;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskScanAntivirus extends AsyncTask<Void, String, List<TaskInfo>> {

    private OnTaskListListener mOnTaskListListener;
    private Context mContext;
    private DataHelper mDataHelper;
    private String pkgName = "";
    private long timeSleep = 0;

    public TaskScanAntivirus(Context mContext, long timeSleep, OnTaskListListener mOnTaskListListener) {
        this.mOnTaskListListener = mOnTaskListListener;
        this.mContext = mContext;
        this.timeSleep = timeSleep;
    }

    public TaskScanAntivirus(Context mContext, String pkgName, long timeSleep, OnTaskListListener mOnTaskListListener) {
        this.mOnTaskListListener = mOnTaskListListener;
        this.mContext = mContext;
        this.timeSleep = timeSleep;
        this.pkgName = pkgName;
    }

    public interface OnTaskListListener {
        void OnResult(List<TaskInfo> lstDangerous, List<TaskInfo> lstVirus);

        void onProgress(String appName, String virusName, String dangerousSize, String progress);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDataHelper = new DataHelper(mContext);
        try {
            mDataHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            mDataHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
    }

    @Override
    protected List<TaskInfo> doInBackground(Void... voids) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = mContext.getPackageManager().queryIntentActivities(mainIntent, 0);
        List<TaskInfo> lstApp = new ArrayList<>();
        int rCode;
        if (!TextUtils.isEmpty(pkgName)) { /*Quét 1 apk cụ thể*/
            TaskInfo mTaskInfo;
            try {
                ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(pkgName, 0);
                if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    mTaskInfo = new TaskInfo(mContext, appInfo);
                    mTaskInfo.setLstPermissonDangerous(getListPermissionDangerous(mContext, mTaskInfo.getPackageName()));
                    rCode = FSEnvi.scanFile(appInfo.sourceDir);
                    if (rCode > 0) {
                        mTaskInfo.setVirusName(mDataHelper.getVname(rCode));
                    }
                    lstApp.add(mTaskInfo);
                }
                Thread.sleep(timeSleep);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < pkgAppsList.size(); i++) {
                ResolveInfo mResolveInfo = pkgAppsList.get(i);
                TaskInfo mTaskInfo = null;
                try {
                    ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mResolveInfo.activityInfo.packageName, 0);
                    if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        mTaskInfo = new TaskInfo(mContext, appInfo);
                        mTaskInfo.setLstPermissonDangerous(getListPermissionDangerous(mContext, mTaskInfo.getPackageName()));
                        rCode = FSEnvi.scanFile(appInfo.sourceDir);
                        if (rCode > 0) {
                            mTaskInfo.setVirusName(mDataHelper.getVname(rCode));
                        }
                        lstApp.add(mTaskInfo);
                    }
                    Thread.sleep(timeSleep);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                float progress = (float) i / (float) pkgAppsList.size();
                publishProgress(mResolveInfo.activityInfo.packageName, mTaskInfo == null ? "" : mTaskInfo.getVirusName(),
                        mTaskInfo == null ? "" : String.valueOf(mTaskInfo.getLstPermissonDangerous().size())
                        , i == pkgAppsList.size() - 1 ? String.valueOf(100) : String.valueOf((int) (progress * 100)));
            }
        }
        Collections.sort(lstApp, (o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
        return lstApp;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (mOnTaskListListener != null)
            mOnTaskListListener.onProgress(values[0], values[1], values[2], values[3]);
    }

    @Override
    protected void onPostExecute(List<TaskInfo> taskInfos) {
        super.onPostExecute(taskInfos);
        if (mOnTaskListListener != null) {
            List<TaskInfo> lstDangerous = new ArrayList<>();
            List<TaskInfo> lstVisrus = new ArrayList<>();
            for (TaskInfo mTaskInfo : taskInfos) {
                if (!TextUtils.isEmpty(mTaskInfo.getVirusName()) && !PreferenceUtils.getListAppWhileVirus().contains(mTaskInfo.getPackageName())) {
                    lstVisrus.add(mTaskInfo);
                } else if (!mTaskInfo.getLstPermissonDangerous().isEmpty()) {
                    lstDangerous.add(mTaskInfo);
                }
            }
            mOnTaskListListener.OnResult(lstDangerous, lstVisrus);
        }
        mDataHelper.close();
    }

    public List<Config.PERMISSION_DANGEROUS> getListPermissionDangerous(Context mContext, String appPackage) {
        List<String> lstPer = getGrantedPermissions(mContext, appPackage);
        List<Config.PERMISSION_DANGEROUS> lstDangerous = new ArrayList<>();
        for (String permission : lstPer) {
            for (Config.PERMISSION_DANGEROUS permissionDangerous : Config.PERMISSION_DANGEROUS.values()) {
                if (permission.equals(permissionDangerous.name)) {
                    lstDangerous.add(permissionDangerous);
                    break;
                }
            }
        }
        return lstDangerous;
    }

    public List<String> getGrantedPermissions(Context mContext, String appPackage) {
        List<String> granted = new ArrayList<>();
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    granted.add(pi.requestedPermissions[i]);
                }
            }
        } catch (Exception e) {
        }
        return granted;
    }

}
