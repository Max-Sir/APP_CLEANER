package com.security.applock.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;

import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;
import com.security.applock.utils.flash.FlashManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;

public class AlarmDuringAntiTheftManager {
    private static final int VIBRATE_PERIOD = 1000;
    private static final int TIME_ON_LEN = 500;
    private static final int TIME_OFF_LEN = 500;
    private Context context;
    private Disposable disposable;
    private FlashManager flashManager;
    private MediaPlayer mPlayer;
    private boolean isActiveAlarm = false;
    private PowerManager.WakeLock screenLock;
    private AudioManager audioManager;
    private int mVolume = -1;
    private Handler handler;
    private Runnable volumeRunable;

    public boolean isActiveAlarm() {
        return isActiveAlarm;
    }

    public AlarmDuringAntiTheftManager(Context context) {
        this.context = context;
        handler = new Handler();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        volumeRunable = () -> {
            if (mVolume != audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, 0);
            }
            handler.post(volumeRunable);
        };
    }

    public void vibrateAntitheft() {
        if (PreferencesHelper.getBoolean(PreferencesHelper.VIBRATE_DURING_ALARM, false)) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            disposable = Flowable.interval(VIBRATE_PERIOD, TimeUnit.MILLISECONDS).repeat().subscribe(aLong -> vibrator.vibrate(500));
        }
    }

    private void flashAntiTheft() {
        if (PreferencesHelper.getBoolean(PreferencesHelper.FLASH_DURING_ALARM, false)) {
            flashManager = FlashManager.getInstance(context, TIME_ON_LEN, TIME_OFF_LEN);
            flashManager.run();
        }
    }

    private void stopAudio() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            handler.removeCallbacks(volumeRunable);
        }
    }

    private void playAudio() {
        int pos = PreferencesHelper.getInt(PreferencesHelper.SETTING_VALUE_TONE, Config.DEFAULT_TONE);
        int rawRes = Config.LIST_TONE[pos];
        mPlayer = MediaPlayer.create(context, rawRes);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float percentVolume = (float) PreferencesHelper.getInt(PreferencesHelper.SETTING_VALUE_SOUND, Config.DEFAULT_SOUND) / 100;
        mVolume = (int) (maxVolume * percentVolume);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, 0);
        mPlayer.start();
        mPlayer.setLooping(true);
        handler.postDelayed(volumeRunable, 10);
    }

    @SuppressLint("InvalidWakeLockTag")
    private void wakeLock() {
        if (screenLock == null) {
            screenLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE))
                    .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        }
        screenLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        screenLock.release();
    }

    public void alarm() {
        isActiveAlarm = true;
        wakeLock();
        playAudio();
        flashAntiTheft();
        vibrateAntitheft();
    }

    public void offAlarmAntiTheft() {
        isActiveAlarm = false;
        stopAudio();
        if (flashManager != null) {
            flashManager.stop();
        }
        if (disposable != null) {
            disposable.dispose();
        }
    }
}

