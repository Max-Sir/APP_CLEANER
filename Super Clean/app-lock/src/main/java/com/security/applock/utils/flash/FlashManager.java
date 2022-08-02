package com.security.applock.utils.flash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper.Callback;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.CAMERA_SERVICE;

public class FlashManager {
    private static FlashManager instance;
    private static Context mContext;
    private Camera camera1, camera2;
    public volatile boolean done;
    private boolean isFlashOn;
    private Parameters params1, params2;
    /*Khoảng thời gian tắt giữa 2 lần nháy*/
    private int time_off;
    /*Khoảng thời gian sáng trong 1 lần*/
    private int time_on;

    private static boolean flagDevice;
    private static int typeFlash = 2;
    private Handler handler = new Handler();
    private Runnable runOn = new Runnable() {
        @Override
        public void run() {
            if (flagDevice) {
                MotorolaFlash mFlash = new MotorolaFlash();
                try {
                    mFlash.enable(true);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                if (VERSION.SDK_INT <= 23) {
                    if (typeFlash == 0) {
                        getCamera1();
                        getCamera2();
                    }
                    if (typeFlash == 1) {
                        getCamera1();
                    }
                    if (typeFlash == 2) {
                        getCamera2();
                    }
                }
                turnOnFlash();
            }
            handler.postDelayed(runOff,time_on);
        }
    };

    private Runnable runOff = new Runnable() {
        @Override
        public void run() {
            if (flagDevice) {
                MotorolaFlash mFlash = new MotorolaFlash();
                try {
                    mFlash.enable(false);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                if (VERSION.SDK_INT <= 23) {
                    if (typeFlash == 0) {
                        getCamera1();
                        getCamera2();
                    }
                    if (typeFlash == 1) {
                        getCamera1();
                    }
                    if (typeFlash == 2) {
                        getCamera2();
                    }
                }
                turnOffFlash();
                turnOffCamera();
            }
            handler.postDelayed(runOn,time_off);
        }
    };

    public int getTime_on() {
        return time_on;
    }

    public void setTime_on(int i) {
        time_on = i;
    }

    public int getTime_off() {
        return time_off;
    }

    public void setTime_off(int i) {
        time_off = i;
    }

    public static FlashManager getInstance(Context context, int onLen, int offLen) {
        mContext = context;
        if (instance == null) {
            instance = new FlashManager();
        }
        instance.setTime_on(onLen);
        instance.setTime_off(offLen);
        flagDevice = Build.MODEL.contains("motorola");
        return instance;
    }

    private FlashManager() {
        flagDevice = Build.MODEL.contains("motorola");
        isFlashOn = false;
        done = true;
        time_on = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        time_off = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        time_on = 500;
        time_off = 500;
    }

    public void run(){
        handler.post(runOn);
    }

    public void turnOffCamera() {
        if (camera1 != null) {
            camera1.stopPreview();
            camera1.release();
            camera1 = null;
        }
        if (camera2 != null) {
            camera2.stopPreview();
            camera2.release();
            camera2 = null;
        }
    }

    public void turnOnFlash() {
        if (VERSION.SDK_INT <= 23) {
            turnOnBelow23();
            return;
        }
        turnOnAbove23();
    }

    public void turnOffFlash() {
        if (VERSION.SDK_INT <= 23) {
            turnOffBelow23();
            return;
        }
        turnOffAbove23();
    }

    private void getCamera1() {
        if (camera1 != null) {
            camera1.release();
            camera1 = null;
        }
        camera1 = openFrontFacingCameraGingerbread();
        params1 = camera1.getParameters();
        try {
            camera1.setPreviewTexture(new SurfaceTexture(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera1.startPreview();

    }

    private void getCamera2() {
        if (camera2 != null) {
            camera2.release();
            camera2 = null;
        }
        camera2 = Camera.open();
        params2 = camera2.getParameters();
        try {
            camera2.setPreviewTexture(new SurfaceTexture(0));
        } catch (IOException ioexception) {
        }
        camera2.startPreview();
    }

    public void stop() {
        if (flagDevice) {
            MotorolaFlash mFlash = new MotorolaFlash();
            try {
                mFlash.enable(false);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            if (VERSION.SDK_INT <= 23) {
                if (typeFlash == 0) {
                    getCamera1();
                    getCamera2();
                }
                if (typeFlash == 1) {
                    getCamera1();
                }
                if (typeFlash == 2) {
                    getCamera2();
                }
            }
            turnOffFlash();
            turnOffCamera();
        }
        handler.removeCallbacks(runOn);
        handler.removeCallbacks(runOff);
    }

    public void turnOffBelow23() {
        if (typeFlash == 0) {
            if (isFlashOn) {
                if (camera1 != null && camera2 != null) {
                    if (params1 != null & params2 != null) {
                        params1.setFlashMode("off");
                        camera1.setParameters(params1);
                        camera1.startPreview();

                        params2.setFlashMode("off");
                        camera2.setParameters(params2);
                        camera2.startPreview();
                        isFlashOn = false;
                    }
                }

            }
            return;

        }
        if (typeFlash == 1) {
            if (isFlashOn) {
                if (camera1 != null) {
                    if (params1 != null) {
                        params1.setFlashMode("off");
                        camera1.setParameters(params1);
                        camera1.startPreview();
                        isFlashOn = false;
                    }
                }
            }
            return;
        }
        if (typeFlash == 2) {
            if (isFlashOn) {
                if (camera2 != null) {
                    if (params2 != null) {
                        params2.setFlashMode("off");
                        camera2.setParameters(params2);
                        camera2.startPreview();
                        isFlashOn = false;
                    }
                }
            }

            return;
        }
    }

    public void turnOnBelow23() {
        if (typeFlash == 0) {
            if (!isFlashOn) {
                if (camera1 != null & camera2 != null) {
                    if (params1 != null & params2 != null) {
                        configFlashParameters(params1);
                        camera1.setParameters(params1);
                        camera1.startPreview();
                        configFlashParameters(params2);
                        camera2.setParameters(params2);
                        camera2.startPreview();
                        isFlashOn = true;
                    }
                }

            }
            return;

        }
        if (typeFlash == 1) {
            if (!isFlashOn) {
                if (camera1 != null) {
                    if (params1 != null) {
                        configFlashParameters(params1);
                        camera1.setParameters(params1);
                        camera1.startPreview();
                        isFlashOn = true;
                    }
                }
            }
            return;
        }
        if (typeFlash == 2) {
            if (camera2 != null) {
                if (params2 != null) {
                    configFlashParameters(params2);
                    camera2.setParameters(params2);
                    camera2.startPreview();
                    isFlashOn = true;
                }
            }
            return;
        }


    }

    public void turnOnAbove23() {
        if (typeFlash == 0) {
            if (!isFlashOn) {
                turnOnBack23();
                turnOnFront23();
                isFlashOn = true;
            }
            return;
        }
        if (typeFlash == 1) {
            if (!isFlashOn) {
                turnOnFront23();
                isFlashOn = true;
            }
            return;
        }

        if (typeFlash == 2) {
            if (!isFlashOn) {
                turnOnBack23();
                isFlashOn = true;
                return;
            }
        }


    }

    public void turnOffAbove23() {
        if (typeFlash == 0) {
            if (isFlashOn) {
                turnOffBack23();
                turnOffFront23();
                isFlashOn = false;
            }
            return;
        }
        if (typeFlash == 1) {
            if (isFlashOn) {
                turnOffFront23();
                isFlashOn = false;
            }
            return;
        }

        if (typeFlash == 2) {
            if (isFlashOn) {
                turnOffBack23();
                isFlashOn = false;
            }
            return;
        }

    }

    public void turnOnBack23() {
        try {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager cameraManager = (CameraManager) mContext.getSystemService(CAMERA_SERVICE);
                cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], true);
            }
        } catch (CameraAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void turnOffBack23() {
        try {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager cameraManager = (CameraManager) mContext.getSystemService(CAMERA_SERVICE);
                cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], false);
            }
        } catch (CameraAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void turnOnFront23() {
        try {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager cameraManager = (CameraManager) mContext.getSystemService(CAMERA_SERVICE);
                cameraManager.setTorchMode(cameraManager.getCameraIdList()[Camera.CameraInfo.CAMERA_FACING_FRONT], true);
            }

        } catch (CameraAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void turnOffFront23() {
        try {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager cameraManager = (CameraManager) mContext.getSystemService(CAMERA_SERVICE);
                cameraManager.setTorchMode(cameraManager.getCameraIdList()[Camera.CameraInfo.CAMERA_FACING_FRONT], false);
            }
        } catch (CameraAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cam = Camera.open(camIdx);
            }
        }

        return cam;
    }

    private void configFlashParameters(Parameters p) {
        Log.v("FlashlightService", "configFlashParameters");
        final List<String> flashes = p.getSupportedFlashModes();
        if (flashes == null) {
            throw new IllegalStateException();
        }
        if (flashes.contains(Parameters.FLASH_MODE_TORCH)) {
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        } else if (flashes.contains(Parameters.FLASH_MODE_ON)) {
            p.setFlashMode(Parameters.FLASH_MODE_ON);
        } else {
            throw new IllegalStateException();
        }
    }

}
