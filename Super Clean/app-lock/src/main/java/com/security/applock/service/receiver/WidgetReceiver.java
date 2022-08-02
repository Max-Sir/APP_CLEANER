package com.security.applock.service.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.security.applock.service.AntiTheftService;
import com.security.applock.service.BackgroundManager;

public class WidgetReceiver extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        runSerivce(context);
    }

    private void runSerivce(Context context) {
        if (BackgroundManager.getInstance(context).canStartService()) {
            BackgroundManager.getInstance(context).startService(AntiTheftService.class);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        runSerivce(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        runSerivce(context);
    }
}
