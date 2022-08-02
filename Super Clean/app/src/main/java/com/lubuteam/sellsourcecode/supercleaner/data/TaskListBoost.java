package com.lubuteam.sellsourcecode.supercleaner.data;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;

import com.lubuteam.sellsourcecode.supercleaner.AppClean;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TaskListBoost extends AsyncTask<Void, TaskInfo, List<TaskInfo>> {
    private static final long TIME_DELAY = 200;
    private Context mContext;
    private PackageManager mPackageManager;
    private OnTaskListListener mOnTaskListListener;
    private long mTotal = 0;

    public interface OnTaskListListener {
        void OnResult(List<TaskInfo> arrList);

        void onProgress(String appName);
    }

    public TaskListBoost(OnTaskListListener onTaskListListener) {
        mContext = AppClean.getInstance();
        mOnTaskListListener = onTaskListListener;
        mPackageManager = mContext.getPackageManager();
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected List<TaskInfo> doInBackground(Void... arg0) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        ArrayList<TaskInfo> arrList = new ArrayList<>();
        int mem = 0;
        if (Build.VERSION.SDK_INT <= 21) {
            Iterator<ActivityManager.RunningAppProcessInfo> iterator = list.iterator();
            do {
                if (!iterator.hasNext()) {
                    break;
                }
                try {
                    if (mPackageManager == null) break;
                    ActivityManager.RunningAppProcessInfo runproInfo = iterator.next();
                    String packagename = runproInfo.processName;
                    ApplicationInfo applicationInfo;
                    applicationInfo = mPackageManager.getApplicationInfo(packagename, 0);
                    if (applicationInfo == null) continue;
                    if (!packagename.contains(mContext.getPackageName()) && applicationInfo != null
                            && Toolbox.isUserApp(applicationInfo)) {
                        if (runproInfo.importance == 130
                                || runproInfo.importance == 300
                                || runproInfo.importance == 100
                                || runproInfo.importance == 400) {

                            TaskInfo info = new TaskInfo(mContext, runproInfo);
                            if (Toolbox.checkLockedItem(mContext, packagename)) {
                                info.setChceked(false);
                            } else {
                                info.setChceked(true);
                            }
                            info.getAppInfo();

                            if (info.isGoodProcess()) {
                                int j = runproInfo.pid;
                                int i[] = new int[1];
                                i[0] = j;
                                Debug.MemoryInfo memInfo[] = am
                                        .getProcessMemoryInfo(i);
                                for (Debug.MemoryInfo mInfo : memInfo) {
                                    int m = mInfo.getTotalPss() * 1024;
                                    info.setMem(m);
                                    mTotal += m;
                                    int jl = mInfo.getTotalPss() * 1024;
                                    int kl = mem;
                                    if (jl > kl)
                                        mem = mInfo.getTotalPss() * 1024;
                                }
                                if (mem > 0) {
                                    Thread.sleep(TIME_DELAY);
                                    arrList.add(info);
                                    publishProgress(info);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            } while (true);
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                for (ActivityManager.RunningServiceInfo runningServiceInfo : am.getRunningServices(Integer.MAX_VALUE)) {
                    try {
                        if (mPackageManager == null) break;
                        PackageInfo packageInfo = mPackageManager.getPackageInfo(runningServiceInfo.service.getPackageName(), PackageManager.GET_ACTIVITIES);
                        if (packageInfo == null) continue;
                        ;
                        ApplicationInfo applicationInfo;
                        applicationInfo = mPackageManager.getApplicationInfo(packageInfo.packageName, 0);
                        if (applicationInfo == null) continue;
                        if (!packageInfo.packageName.contains(mContext.getPackageName()) && applicationInfo != null && Toolbox.isUserApp(applicationInfo)) {
                            TaskInfo info = new TaskInfo(mContext, applicationInfo);
                            if (Toolbox.checkLockedItem(mContext, mContext.getPackageName())) {
                                info.setChceked(false);
                            } else {
                                info.setChceked(true);
                            }

                            if (info.isGoodProcess()) {
                                int j = runningServiceInfo.pid;
                                int i[] = new int[1];
                                i[0] = j;
                                Debug.MemoryInfo memInfo[] = am
                                        .getProcessMemoryInfo(i);
                                for (Debug.MemoryInfo mInfo : memInfo) {
                                    int m = mInfo.getTotalPss() * 1024;
                                    info.setMem(m);
                                    mTotal += m;
                                    int jl = mInfo.getTotalPss() * 1024;
                                    int kl = mem;
                                    if (jl > kl)
                                        mem = mInfo.getTotalPss() * 1024;
                                }
                                if (mem > 0) {
                                    Thread.sleep(TIME_DELAY);
                                    arrList.add(info);
                                    publishProgress(info);
                                }
                            }
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            } else {
                UsageStatsManager usage = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - (60 * 1000), time);
                for (UsageStats usageStats : stats) {
                    try {
                        if (mPackageManager == null) break;
                        PackageInfo packageInfo = mPackageManager.getPackageInfo(usageStats.getPackageName(), PackageManager.GET_ACTIVITIES);
                        if (packageInfo == null) continue;
                        ;
                        ApplicationInfo applicationInfo;
                        applicationInfo = mPackageManager.getApplicationInfo(packageInfo.packageName, 0);
                        if (applicationInfo == null) continue;
                        if (!packageInfo.packageName.contains(mContext.getPackageName()) && applicationInfo != null && Toolbox.isUserApp(applicationInfo)) {
                            TaskInfo info = new TaskInfo(mContext, applicationInfo);
                            if (Toolbox.checkLockedItem(mContext, mContext.getPackageName())) {
                                info.setChceked(false);
                            } else {
                                info.setChceked(true);
                            }
                            Thread.sleep(TIME_DELAY);
                            arrList.add(info);
                            publishProgress(info);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            HashMap<String, TaskInfo> hashMap = new HashMap<>();
            for (TaskInfo taskInfo : arrList) {
                if (taskInfo.getMem() != 0) {
                    if (hashMap.containsKey(taskInfo.getPackageName())) {
                        TaskInfo old = hashMap.get(taskInfo.getPackageName());
                        if (old.getMem() < taskInfo.getMem()) {
                            hashMap.put(taskInfo.getPackageName(), taskInfo);
                        }
                    } else {
                        hashMap.put(taskInfo.getPackageName(), taskInfo);
                    }
                }
                hashMap.put(taskInfo.getPackageName(), taskInfo);
            }
            Collection<TaskInfo> collection = hashMap.values();
            return new ArrayList(collection);
        }

        return arrList;
    }

    @Override
    protected void onProgressUpdate(TaskInfo... values) {
        super.onProgressUpdate(values);
        if (null != mOnTaskListListener) {
            mOnTaskListListener.onProgress(values[0].getTitle());
        }
    }

    @Override
    protected void onPostExecute(List<TaskInfo> taskInfos) {
        super.onPostExecute(taskInfos);
        if (null != mOnTaskListListener) {
            mOnTaskListListener.OnResult(taskInfos);
        }
    }

    private long getAvaiableRam(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            activityManager.getMemoryInfo(mi);
        }
        return mi.availMem;
    }


}

