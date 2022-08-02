package com.security.applock.utils.flash;

import android.os.IBinder;

import java.lang.reflect.Method;

public class MotorolaFlash {

    private Object svc;
    private Method getFlashlightEnabled;
    private Method setFlashlightEnabled;

    @SuppressWarnings("unchecked")
    public MotorolaFlash() {
        try {
            Class sm = Class.forName("android.os.ServiceManager");
            Object hwBinder = sm.getMethod("getService", String.class).invoke(null, "hardware");

            Class hwsstub = Class.forName("android.os.IHardwareService$Stub");
            Method asInterface = hwsstub.getMethod("asInterface", IBinder.class);
            svc = asInterface.invoke(null, (IBinder) hwBinder);

            Class proxy = svc.getClass();

            getFlashlightEnabled = proxy.getMethod("getFlashlightEnabled");
            setFlashlightEnabled = proxy.getMethod("setFlashlightEnabled", boolean.class);
        } catch (Exception e) {

        }
    }

    private static String mAuth = "436F70797269676874206279204C7562755465616D2E636F6D5F2B3834393731393737373937";

    public boolean isEnabled() {
        try {
            return getFlashlightEnabled.invoke(svc).equals(true);
        } catch (Exception e) {
            return false;
        }
    }

    public void enable(boolean tf) {
        try {
            setFlashlightEnabled.invoke(svc, tf);
        } catch (Exception e) {
        }
    }
}
