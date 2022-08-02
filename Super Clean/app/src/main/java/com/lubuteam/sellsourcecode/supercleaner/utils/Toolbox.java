package com.lubuteam.sellsourcecode.supercleaner.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.screen.gameboost.GameBoostActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.main.MainActivity;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Toolbox {

    public static int getHeightStatusBar(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static int getHeightStatusBar(Context context, boolean hasRebit) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (hasRebit)
            /*lấy height statusbar của những device tai thỏ, device ko có trả về 0*/
            return result > 88 ? result : 0;
        /*lấy height statusbar của những device không tai thỏ, device tai thỏ trả về 0*/
        return result <= 88 ? result : 0;
    }

    public static void setStatusBarHomeTransparent(FragmentActivity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.setNavigationBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            //make fully Android Transparent Status bar
            setWindowFlag((AppCompatActivity) activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(AppCompatActivity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static long getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        long totRam = 0;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Integer.valueOf(value);
            // totRam = totRam / 1024;


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return (totRam * 1024);
    }

    public static boolean isUserApp(ApplicationInfo ai) {
        int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
        return (ai.flags & mask) == 0;
    }

    public static boolean checkLockedItem(Context context, String checkApp) {
        boolean check = false;
        List<String> locked = PreferenceUtils.getListAppLocked();
        if (locked != null) {
            for (String lock : locked) {
                if (lock.equals(checkApp)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    public static int getDesity(Context context, int i) {
        return (int) (((float) i) * context.getResources().getDisplayMetrics().density);
    }

    public static void animationTransColor(int fromColor, int toColor, long duration, View mView) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(fromColor, toColor);
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(valueAnimator -> mView.setBackgroundColor((Integer) valueAnimator.getAnimatedValue()));
        anim.setDuration(duration);
        anim.start();
    }

    public static int[] getProgressDrawableColors(Context mContext) {
        int[] colors = new int[4];
        colors[0] = mContext.getResources().getColor(R.color.red);
        colors[1] = mContext.getResources().getColor(R.color.blue);
        colors[2] = mContext.getResources().getColor(R.color.yellow);
        colors[3] = mContext.getResources().getColor(R.color.green);
        return colors;
    }

    public static boolean isOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static int getHeightScreen(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }

    public static int getHeightNavigationbar(Context context) {
        Resources resources = context.getResources();
        if (!isNavigationBarAvailable())
            return 0;
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static boolean isNavigationBarAvailable() {
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        return (!(hasBackKey && hasHomeKey));
    }

    public static void showKeybroad(Context mContext, EditText mEditText) {
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static List<TaskInfo> getLstTaskFillterIgnore(List<TaskInfo> lstInput) {
        List<TaskInfo> lstOutput = new ArrayList<>();
        List<String> lstIgnore = PreferenceUtils.getListAppIgnore();
        if (lstInput.isEmpty())
            return lstOutput;
        if (lstIgnore.isEmpty())
            return lstInput;
        for (TaskInfo mTaskInfo : lstInput) {
            boolean isSkip = false;
            for (String pkgName : lstIgnore) {
                if (mTaskInfo.getPackageName().equals(pkgName)) {
                    isSkip = true;
                    break;
                }
            }
            if (!isSkip)
                lstOutput.add(mTaskInfo);
        }
        return lstOutput;
    }

    public static String endCode(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String decode(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String getStringByLocalPlus17(Context context, int resId, String locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));
        return context.createConfigurationContext(configuration).getResources().getString(resId);
    }

    public static void hideSoftKeyboard(@Nullable Activity activity) {
        if (activity != null) {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }

    public static void creatShorCutGameBoost(Context mContext) {
        if (Build.VERSION.SDK_INT < 26) {
            Intent intent = new Intent(mContext, GameBoostActivity.class);
            Intent intent2 = new Intent();
            intent2.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            intent2.putExtra(Intent.EXTRA_SHORTCUT_NAME, mContext.getString(R.string.game_booster));
            intent2.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(mContext, R.drawable.ic_game_booster));
            intent2.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            mContext.sendBroadcast(intent2);
            Toast.makeText(mContext, mContext.getString(R.string.notification_created_shortcut, mContext.getString(R.string.app_name)), Toast.LENGTH_LONG).show();
            return;
        }
        ShortcutManager shortcutManager = mContext.getSystemService(ShortcutManager.class);
        if (shortcutManager.isRequestPinShortcutSupported()) {
            Intent intent3 = new Intent(mContext, GameBoostActivity.class);
            intent3.setAction(Intent.ACTION_MAIN);
            ShortcutInfo build = new ShortcutInfo.Builder(mContext, GameBoostActivity.class.getName())
//                    .setIcon(Icon.createWithResource(mContext, R.drawable.ic_game_booster))
                    .setIcon(Icon.createWithResource(mContext, R.drawable.ic_game_booster1))
//                    .setIcon(Icon.createWithResource(mContext, R.drawable.ic_logo))
                    .setIntent(intent3).setShortLabel(mContext.getString(R.string.game_booster)).build();
            shortcutManager.requestPinShortcut(build, PendingIntent.getBroadcast(mContext, 0, shortcutManager.createShortcutResultIntent(build), 0).getIntentSender());
        }
    }

    public static void creatShorCutNormal(Context mContext) {
        if (Build.VERSION.SDK_INT < 26) {
            Intent intent = new Intent(mContext, MainActivity.class);
            Intent intent2 = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent2.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            intent2.putExtra(Intent.EXTRA_SHORTCUT_NAME, mContext.getString(R.string.app_name));
            intent2.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(mContext, R.drawable.ic_logo));
            intent2.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            mContext.sendBroadcast(intent2);
            Toast.makeText(mContext, mContext.getString(R.string.notification_created_shortcut, mContext.getString(R.string.app_name)), Toast.LENGTH_LONG).show();
            return;
        }
        ShortcutManager shortcutManager = mContext.getSystemService(ShortcutManager.class);
        if (shortcutManager.isRequestPinShortcutSupported()) {
            Intent intent3 = new Intent(mContext, MainActivity.class);
            intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent3.setAction(Intent.ACTION_MAIN);
            ShortcutInfo build = new ShortcutInfo.Builder(mContext, MainActivity.class.getName())
                    .setIcon(Icon.createWithResource(mContext, R.drawable.ic_logo))
                    .setIntent(intent3).setShortLabel(mContext.getString(R.string.app_name)).build();
            shortcutManager.requestPinShortcut(build, PendingIntent.getBroadcast(mContext, 0, shortcutManager.createShortcutResultIntent(build), 0).getIntentSender());
        }
    }

    public static Bitmap loadAppIcon(Context context, String pkgName) {
        ApplicationInfo ai = loadAppInfo(context, pkgName);
        if (ai != null) {
            return drawableToBitmap(context, ai.loadIcon(context.getPackageManager()));
        }
        return drawableToBitmap(context, context.getResources().getDrawable(R.drawable.ic_app_uninstall));
    }

    public static ApplicationInfo loadAppInfo(Context context, String pkgName) {
        try {
            return context.getPackageManager().getApplicationInfo(pkgName, 1);
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap drawableToBitmap(Context context, Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        float density = context.getResources().getDisplayMetrics().density;
        int width = (int) (((float) drawable.getIntrinsicWidth()) * density);
        int height = (int) (((float) drawable.getIntrinsicHeight()) * density);
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static String formatSize(long size) {
        if (size <= 0)
            return "";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer);
        }
    }

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getDistanceTime(Context mContext, long time) {
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        long dicstance = currentTime - time;
        if (dicstance < 60 * 1000) {
            return mContext.getString(R.string.just_now_time);
        } else if (dicstance < 60 * 60 * 1000) {
            int minute = (int) (dicstance / (60 * 1000));
            return mContext.getString(R.string.minute_ago, String.valueOf(minute));
        } else if (dicstance < 24 * 60 * 60 * 1000) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            return simpleDateFormat.format(new Date(dicstance));
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return simpleDateFormat.format(new Date(dicstance));
        }
    }

    public static int getDimension(float f) {
        return (int) TypedValue.applyDimension(1, f, getMetrics());
    }

    public static DisplayMetrics getMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    public static int getDisplayMetrics(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        return (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) ? Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) : Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    }


    public static Bitmap scalleBitmap(Context context, String str, Bitmap bitmap) {
        try {
            Drawable applicationIcon = context.getPackageManager().getApplicationIcon(str);
            if (applicationIcon != null) {
                applicationIcon.setAlpha(255);
                int a2 = getDisplayMetrics(context, 64.0f);
                applicationIcon.setBounds(new Rect(0, 0, a2, a2));
                return drawableToBitmap(applicationIcon);
            }
            throw new NullPointerException();
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static boolean arrBitmap(Bitmap... bitmapArr) {
        for (Bitmap bitmap : bitmapArr) {
            if (!(bitmap != null && !bitmap.isRecycled())) {
                return false;
            }
        }
        return true;
    }

    public static String longToDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateText = df2.format(date);
        return dateText;
    }

    public static String getApkSize(Context context, String packageName)
            throws PackageManager.NameNotFoundException {
        long size = new File(context.getPackageManager().getApplicationInfo(
                packageName, 0).publicSourceDir).length();
        return formatSize(size);
    }

    public static void setTextFromSize(long size, TextView tvNumber, TextView tvType) {

        if (size <= 0) {
            tvNumber.setText(String.valueOf(0.00));
            tvType.setText("MB");
            return;
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        tvNumber.setText(String.valueOf(new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups))));

        tvType.setText(units[digitGroups]);
    }

    public static int getTempC(int i) {
        return (int) (Math.ceil((i / 10f) * 100) / 100);
    }

    public static String intTimeOff(int time) {
        int dNDEndTime = time;
        int i = dNDEndTime / 100;
        dNDEndTime %= 100;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%02d", Integer.valueOf(dNDEndTime)));
        return stringBuilder.toString();
    }

    public static String intTimeOn(int time) {
        int dNDStartTime = time;
        int i = dNDStartTime / 100;
        dNDStartTime %= 100;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02d", Integer.valueOf(i)));
        stringBuilder.append(":");
        stringBuilder.append(String.format("%02d", Integer.valueOf(dNDStartTime)));
        return stringBuilder.toString();
    }

    public static long getTimeAlarmJunkFile(boolean firstStart) {
        if (firstStart) {
            return new Random().nextInt(AlarmUtils.TIME_JUNK_FILE_FIRST_START) * 60 * 1000;
        } else {
            int userSelect = PreferenceUtils.getTimeRemindJunkFile();
            long oneDay = 24 * 60 * 60 * 1000;
            return userSelect * oneDay;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else {
            return false;
        }
    }

}
