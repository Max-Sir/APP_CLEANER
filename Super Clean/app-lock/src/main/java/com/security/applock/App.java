package com.security.applock;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import com.security.applock.service.AntiTheftService;
import com.security.applock.service.BackgroundManager;
import com.security.applock.service.jobScheduler.JobSchedulerService;
import com.security.applock.ui.BaseActivity;
import com.security.applock.utils.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class App {

    private static App instance;
    private boolean forceLockScreen;
    private Context context;
    private static List<BaseActivity> activityList;

    public boolean isForceLockScreen() {
        return forceLockScreen;
    }

    public void setForceLockScreen(boolean forceLockScreen) {
        this.forceLockScreen = forceLockScreen;
    }

    public static App getInstace() {
        if (instance == null)
            instance = new App();
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public void init(Context context){
        this.context = context;
        PreferencesHelper.init(context);
        if (BackgroundManager.getInstance(context).canStartService()) {
            BackgroundManager.getInstance(context).startService(AntiTheftService.class);
        }
        startJobScheduler();
        activityList = new ArrayList<>();
    }

    public void doForCreate(BaseActivity activity) {
        activityList.add(activity);
    }

    public void doForFinish(BaseActivity activity) {
        activityList.remove(activity);
    }

    private void startJobScheduler() {
        if (Build.VERSION.SDK_INT >= 21) {
            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(context, JobSchedulerService.class));
            builder.setPeriodic(1000);
            builder.setPersisted(true);
            ((JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE)).schedule(builder.build());
        }

    }

    public void clearAllActivity() {
        for (BaseActivity activity : activityList) {
            if (activity != null)
                activity.clear();
        }
        activityList.clear();
    }

}
