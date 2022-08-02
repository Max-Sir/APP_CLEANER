package com.lubuteam.sellsourcecode.supercleaner.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BaseApp {
    public static boolean a = false;
    public static boolean b = false;
    public static boolean c = false;
    private static Handler f = new Handler();
    private Application d;
    private LocalBroadcastManager e;

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final BaseApp a = new BaseApp();
    }

    public static Handler a() {
        return f;
    }

    private BaseApp() {
    }

    public static final BaseApp b() {
        return LazyHolder.a;
    }

    public void a(Application application) {
        this.d = application;
        f = new Handler();
        this.e = LocalBroadcastManager.getInstance((Context) this.d);
    }

    public Application c() {
        return this.d;
    }

    public static boolean a(Runnable runnable, long j) {
        if (f == null) {
            return false;
        }
        f.postDelayed(runnable, j);
        return true;
    }

    public static boolean a(Runnable runnable) {
        if (f == null) {
            return false;
        }
        f.post(runnable);
        return true;
    }
}
