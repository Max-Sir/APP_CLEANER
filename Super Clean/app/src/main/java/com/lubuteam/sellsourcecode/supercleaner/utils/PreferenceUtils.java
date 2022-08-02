package com.lubuteam.sellsourcecode.supercleaner.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lubuteam.sellsourcecode.supercleaner.model.LstPkgNameTaskInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PreferenceUtils {

    private static final String WHITE_LIST = "whitelist";
    private static final String LAST_TIME_BOOST = "last time used function";
    private static final String LIST_APP_SKIP = "list app skip";
    private static final String LIST_APP_WHILE_VIRUS = "list app while virus";
    private static final String LIST_APP_GAME_BOOST = "list app game boost";
    private static final String ANSWER_SECUTIRY_QUESTION = "answer secutiry question";
    private static final String HIDE_NOFIFICATION = "hide notification";
    private static final String TIME_INSTALL_APP = "time install app";
    private static final String FIRST_OPEN_FUNCTION = "first open function";
    private static final String ON_OFF_SMART_CHARGER = "on off smart charger";
    public static final String RECHARGE_WIFI = "recharger wifi";
    public static final String RECHARGE_BRIGHTNESS = "recharger brightness";
    public static final String RECHARGE_BLUETOOTH = "recharger bluetooth";
    public static final String RECHARGE_SYNC = "recharger syncchonized";
    public static final String BATTERY_FULL = "notification battery full";
    public static final String POWER_CONNECTED = "power connected";
    public static final String TURN_ON_NOTI_BATTERY_FULL = "turn on notifi battery full";
    public static final String TURN_ON_CLEAN_NOTIFICATION = "turn on clean notification";
    private static final String LIST_APP_CLEAN_NOTIFI = "list app clean notification";
    private static final String SCAN_UNINSTALL_APK = "scan uninstaill apk";
    private static final String SCAN_INSTALL_APK = "scan isntall apk";
    private static final String RPOTECTION_REAL_TIME = "protection real time";
    private static final String TIME_ALARM_PHONE_BOOST = "time alarm phone boost";
    private static final String TIME_ALARM_CPU_COOLER = "time alarm cpu cooller";
    private static final String TIME_ALARM_BATTERY_SAVE = "time alarm battery save";
    private static final String SHOW_HIDE_NOTIFICATION_MANAGER = "show hide notification manager";
    private static final String REMINDER_JUNK_FILE = "reminder junk file";
    private static final String NOT_DISSSTURB_START = "not dissturb start";
    private static final String NOT_DISSSTURB_END = "not dissturb end";
    private static final String NOT_DISSSTURB = "not dissturb";
    private static final String PKG_CLEAN_CACHE = "package clean cache";

    private static SharedPreferences preferences;
    private static Gson gson;

    public static void init(Context mContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        gson = new Gson();
    }

    public static ArrayList<String> getListAppLocked() {
        List<String> locked;
        if (preferences.contains(WHITE_LIST)) {
            String jsonLocked = preferences.getString(WHITE_LIST, null);
            String[] lockedItems = gson.fromJson(jsonLocked, String[].class);
            locked = Arrays.asList(lockedItems);
            locked = new ArrayList<>(locked);
        } else
            return null;
        return (ArrayList<String>) locked;
    }

    public static void saveListAppLocked(List<String> lockedApp) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        String jsonLockedApp = gson.toJson(lockedApp);
        editor.putString(WHITE_LIST, jsonLockedApp);
        editor.apply();
    }

    public static long getLastTimeUseFunction(Config.FUNCTION mFunction) {
        return preferences.getLong(LAST_TIME_BOOST + mFunction.id, 0);
    }

    public static boolean checkLastTimeUseFunction(Config.FUNCTION mFunction) {
        long timeSpace = System.currentTimeMillis() - PreferenceUtils.getLastTimeUseFunction(mFunction);
        return timeSpace > Config.TIME_ALLOW_BOOOST;
//        return true;
    }

    public static void setLastTimeUseFunction(Config.FUNCTION mFunction) {
        preferences.edit().putLong(LAST_TIME_BOOST + mFunction.id, System.currentTimeMillis()).apply();
    }

    public static void setListAppIgnore(List<String> lstApp) {
        Gson gson = new Gson();
        String data = gson.toJson(new LstPkgNameTaskInfo(lstApp));
        preferences.edit().putString(LIST_APP_SKIP, data).apply();
    }

    public static List<String> getListAppIgnore() {
        String data = preferences.getString(LIST_APP_SKIP, "");
        Gson gson = new Gson();
        LstPkgNameTaskInfo object = gson.fromJson(data, LstPkgNameTaskInfo.class);
        return object != null ? object.getLst() : new ArrayList<>();
    }

    public static void setListAppWhileVirus(List<String> lstApp) {
        Gson gson = new Gson();
        String data = gson.toJson(new LstPkgNameTaskInfo(lstApp));
        preferences.edit().putString(LIST_APP_WHILE_VIRUS, data).apply();
    }

    public static List<String> getListAppWhileVirus() {
        String data = preferences.getString(LIST_APP_WHILE_VIRUS, "");
        Gson gson = new Gson();
        LstPkgNameTaskInfo object = gson.fromJson(data, LstPkgNameTaskInfo.class);
        return object != null ? object.getLst() : new ArrayList<>();
    }

    public static void setAnswerSecutiryQuestion(String answer) {
        preferences.edit().putString(ANSWER_SECUTIRY_QUESTION, answer).apply();
    }

    public static String getAnswerSecutiryQuestion() {
        return preferences.getString(ANSWER_SECUTIRY_QUESTION, "");
    }

    public static void setListAppGameBoost(List<String> lstApp) {
        Gson gson = new Gson();
        String data = gson.toJson(new LstPkgNameTaskInfo(lstApp));
        preferences.edit().putString(LIST_APP_GAME_BOOST, data).apply();
    }

    public static List<String> getListAppGameBoost() {
        String data = preferences.getString(LIST_APP_GAME_BOOST, "");
        Gson gson = new Gson();
        LstPkgNameTaskInfo object = gson.fromJson(data, LstPkgNameTaskInfo.class);
        return object != null ? object.getLst() : new ArrayList<>();
    }

    public static void setHideNotification(boolean isHide) {
        preferences.edit().putBoolean(HIDE_NOFIFICATION, isHide).apply();
    }

    public static boolean isHideNotification() {
        return preferences.getBoolean(HIDE_NOFIFICATION, false);
    }

    public static long getTimeInstallApp() {
        return preferences.getLong(TIME_INSTALL_APP, 0);
    }

    public static void setTimeInstallApp(long time) {
        preferences.edit().putLong(TIME_INSTALL_APP, time).apply();
    }

    public static boolean isFirstUsedFunction(Config.FUNCTION mFunction) {
        return preferences.getBoolean(FIRST_OPEN_FUNCTION + mFunction.id, true);
    }

    public static void setFirstUsedFunction(Config.FUNCTION mFunction) {
        preferences.edit().putBoolean(FIRST_OPEN_FUNCTION + mFunction.id, false).apply();
    }

    public static boolean isOnSmartCharger() {
        return preferences.getBoolean(ON_OFF_SMART_CHARGER, false);
    }

    public static void setSmartCharger(boolean isTurnOn) {
        preferences.edit().putBoolean(ON_OFF_SMART_CHARGER, isTurnOn).apply();
    }

    public static void setValueRecharger(String valueName, boolean isTurnOn) {
        preferences.edit().putBoolean(valueName, isTurnOn).apply();
    }

    public static boolean getValueRecharger(String valueName) {
        return preferences.getBoolean(valueName, false);
    }

    public static void setTimeBatteryFull() {
        preferences.edit().putLong(BATTERY_FULL, System.currentTimeMillis()).apply();
    }

    public static boolean checkTimeBatteryFull() {
        long timeSpace = System.currentTimeMillis() - preferences.getLong(BATTERY_FULL, 0);
        return timeSpace > Config.TIME_BATTERY_FULL;
    }

    public static void setPowerConnected(boolean isConnected) {
        preferences.edit().putBoolean(POWER_CONNECTED, isConnected).apply();
    }

    public static boolean isPowerConnected() {
        return preferences.getBoolean(POWER_CONNECTED, false);
    }

    public static void setNotifiBatteryFull(boolean isTurnOn) {
        preferences.edit().putBoolean(TURN_ON_NOTI_BATTERY_FULL, isTurnOn).apply();
    }

    public static boolean isNotifiBatteryFull() {
        return preferences.getBoolean(TURN_ON_NOTI_BATTERY_FULL, false);
    }

    public static void setCleanNotification(boolean isTurnOn) {
        preferences.edit().putBoolean(TURN_ON_CLEAN_NOTIFICATION, isTurnOn).apply();
    }

    public static boolean isTurnOnCleanNotification() {
        return preferences.getBoolean(TURN_ON_CLEAN_NOTIFICATION, false);
    }

    public static void setListAppCleanNotifi(List<String> lstApp) {
        Gson gson = new Gson();
        String data = gson.toJson(new LstPkgNameTaskInfo(lstApp));
        preferences.edit().putString(LIST_APP_CLEAN_NOTIFI, data).apply();
    }

    public static List<String> getListAppCleanNotifi() {
        String data = preferences.getString(LIST_APP_CLEAN_NOTIFI, "");
        Gson gson = new Gson();
        LstPkgNameTaskInfo object = gson.fromJson(data, LstPkgNameTaskInfo.class);
        return object != null ? object.getLst() : new ArrayList<>();
    }

    public static void setScanUninstaillApk(boolean isScan) {
        preferences.edit().putBoolean(SCAN_UNINSTALL_APK, isScan).apply();
    }

    public static boolean isScanUninstaillApk() {
        return preferences.getBoolean(SCAN_UNINSTALL_APK, false);
    }

    public static void setScaninstaillApk(boolean isScan) {
        preferences.edit().putBoolean(SCAN_INSTALL_APK, isScan).apply();
    }

    public static boolean isScanInstaillApk() {
        return preferences.getBoolean(SCAN_INSTALL_APK, false);
    }

    public static void setProtectionRealTime(boolean isScan) {
        preferences.edit().putBoolean(RPOTECTION_REAL_TIME, isScan).apply();
    }

    public static boolean isProtectionRealTime() {
        return preferences.getBoolean(RPOTECTION_REAL_TIME, false);
    }

    public static void setTimeAlarmPhoneBoost(long time) {
        preferences.edit().putLong(TIME_ALARM_PHONE_BOOST, time).apply();
    }

    public static long getTimeAlarmPhoneBoost() {
        return preferences.getLong(TIME_ALARM_PHONE_BOOST, 0);
    }

    public static void setTimeAlarmCpuCooler(long time) {
        preferences.edit().putLong(TIME_ALARM_CPU_COOLER, time).apply();
    }

    public static long getTimeAlarmCpuCooler() {
        return preferences.getLong(TIME_ALARM_CPU_COOLER, 0);
    }

    public static void setTimeAlarmBatterySave(long time) {
        preferences.edit().putLong(TIME_ALARM_BATTERY_SAVE, time).apply();
    }

    public static long getTimeAlarmBatterySave() {
        return preferences.getLong(TIME_ALARM_BATTERY_SAVE, 0);
    }

    public static boolean isShowHideNotificationManager() {
        return preferences.getBoolean(SHOW_HIDE_NOTIFICATION_MANAGER, true);
    }

    public static void setShowHideNotificationManager(boolean isShow) {
        preferences.edit().putBoolean(SHOW_HIDE_NOTIFICATION_MANAGER, isShow).apply();
    }

    public static void setTimeRemindJunkFile(int day) {
        preferences.edit().putInt(REMINDER_JUNK_FILE, day).apply();
    }

    public static int getTimeRemindJunkFile() {
        return preferences.getInt(REMINDER_JUNK_FILE, 1);
    }

    public static int getDNDStart() {
        return preferences.getInt(NOT_DISSSTURB_START, 2200);
    }

    public static void setDNDStart(int time) {
        preferences.edit().putInt(NOT_DISSSTURB_START, time).apply();
    }

    public static int getDNDEnd() {
        return preferences.getInt(NOT_DISSSTURB_END, 800);
    }

    public static void setDNDEnd(int time) {
        preferences.edit().putInt(NOT_DISSSTURB_END, time).apply();
    }

    public static boolean isDND() {
        return preferences.getBoolean(NOT_DISSSTURB, true);
    }

    public static void setDND(boolean active) {
        preferences.edit().putBoolean(NOT_DISSSTURB, active).apply();
    }

    public static void setTimePkgCleanCache(String pkgName) {
        preferences.edit().putLong(PKG_CLEAN_CACHE + "_" + pkgName, System.currentTimeMillis()).apply();
    }

    public static boolean isCleanCache(String pkgName) {
        return System.currentTimeMillis() - preferences.getLong(PKG_CLEAN_CACHE + "_" + pkgName, 0) >((new Random().nextInt(30)+10)  * 60 * 1000);
    }

}
