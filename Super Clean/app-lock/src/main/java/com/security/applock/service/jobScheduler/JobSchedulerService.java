package com.security.applock.service.jobScheduler;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.security.applock.service.AntiTheftService;
import com.security.applock.service.BackgroundManager;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;


public class JobSchedulerService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("XXX", "JobSchedulerService onStartJob");
        if (BackgroundManager.getInstance(this).canStartService()) {
            BackgroundManager.getInstance(this).startService(AntiTheftService.class);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("XXX", "JobSchedulerService onStartJob");
        return false;
    }
}
