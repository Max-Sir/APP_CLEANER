package com.security.applock.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.intuit.sdp.BuildConfig;
import com.security.applock.R;
import com.security.applock.service.KeepLiveAccessibilityService;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Toolbox {

    public static void setStatusBarHomeTransparent(FragmentActivity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
//            window.setNavigationBarColor(Color.BLACK);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            window.setNavigationBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            //make fully Android Transparent Status bar
            setWindowFlag((AppCompatActivity) activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
//            window.setStatusBarColor(activity.getResources().getColor(R.color.color_2D9CFF));
//            window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setStatusBarHomeTransfer(FragmentActivity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.setNavigationBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            //make fully Android Transparent Status bar
            setWindowFlag((AppCompatActivity) activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
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

    public static int getHeightStatusBar(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }

    }

    public static void showSoftKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    public static void openURL(Context mContext, String url) {
        if (!TextUtils.isEmpty(url)) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    public static void RateApp(Context mContext) {
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
    }

    public static DividerItemDecoration simpleDividerItemDecoration(Context context,
                                                                    int orientation,
                                                                    float dpSize,
                                                                    @ColorInt int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);

        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, context.getResources().getDisplayMetrics());
        gradientDrawable.setSize(size, size);
        gradientDrawable.setColor(color);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, orientation);
        dividerItemDecoration.setDrawable(gradientDrawable);

        return dividerItemDecoration;
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static boolean isPackageInstalled(String packageName, Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getVersionName(Context mContext) {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "1.0";
        }
    }

    public static boolean isNormalText(String txt) {
        if (TextUtils.isEmpty(txt))
            return false;
        return txt.matches("^[A-Za-z0-9_.]+$");
    }

    public static void shareApp(Context context) {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi\nI am using " + context.getString(R.string.app_name) + " app on my android phone\n\nDowload:\n" + "https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.share_friends)));
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

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer);
        }
    }

    public static void feedback(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + Config.EMAIL));
            intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name) + " " + context.getString(R.string.feed_back));
            String out = "Manufaceturer: " + Toolbox.getDeviceName() + "\n" +
                    "OS: " + Build.VERSION.SDK_INT + "\n" +
                    "Version code: " + BuildConfig.VERSION_CODE + "\n" +
                    "Model: " + Build.MODEL;
            intent.putExtra(Intent.EXTRA_TEXT, out);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //TODO smth
        }
    }

    public static String getDistanceTime(long time) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(new Date(time));
        c2.setTime(new Date(System.currentTimeMillis()));

        int yearDiff = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        int monthDiff = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        int dayDiff = c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpleDateFormat = null;
        if (yearDiff != 0) {
            simpleDateFormat = new SimpleDateFormat("MMM dd yyyy");
            return simpleDateFormat.format(c1.getTime());
        } else if (monthDiff != 0) {
            simpleDateFormat = new SimpleDateFormat("MMM dd");
        } else {
            simpleDateFormat = new SimpleDateFormat("hh:mm");
        }
        return simpleDateFormat.format(c1.getTime());
    }

    public static String secondToTime(int seconds) {
        int minute;
        int hour = 0;
        int second;
        if (seconds == 0)
            return "00:00";
        else if (seconds > 60 && seconds < 60 * 60) {
            minute = seconds / 60;
            second = seconds - minute * 60;
        } else {
            hour = seconds / (60 * 60);
            minute = (seconds - hour * 60 * 60) / 60;
            second = seconds - hour * 60 * 60 - minute * 60;
        }
        NumberFormat formatter = new DecimalFormat("00");
        return formatter.format(hour) + ":" + formatter.format(minute) + ":" + formatter.format(second);
    }

    public static int getViewInset(View view) {
        if (view == null || Build.VERSION.SDK_INT < 21) {
            return 0;
        }
        try {
            Field mAttachInfoField = View.class.getDeclaredField("mAttachInfo");
            mAttachInfoField.setAccessible(true);
            Object mAttachInfo = mAttachInfoField.get(view);
            if (mAttachInfo != null) {
                Field mStableInsetsField = mAttachInfo.getClass().getDeclaredField("mStableInsets");
                mStableInsetsField.setAccessible(true);
                Rect insets = (Rect) mStableInsetsField.get(mAttachInfo);
                return insets.bottom;
            }
        } catch (Exception e) {
            // FileLog.e("tmessages", e);
        }
        return 0;
    }

    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static Bitmap screenShortView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    public static void shareImage(Context context, String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share image");
        ArrayList<Uri> files = new ArrayList<>();
        File file = new File(path);
        Uri uriImage;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            uriImage = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        } else {
            uriImage = Uri.fromFile(file);
        }
        files.add(uriImage);
        intent.setType("image/jpeg");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        context.startActivity(Intent.createChooser(intent, "Share image"));
    }

    public static int[] getProgressDrawableColors(Context mContext) {
        int[] colors = new int[4];
        colors[0] = mContext.getResources().getColor(R.color.red);
        colors[1] = mContext.getResources().getColor(R.color.blue);
        colors[2] = mContext.getResources().getColor(R.color.yellow);
        colors[3] = mContext.getResources().getColor(R.color.green);
        return colors;
    }

    public static Drawable getdIconApplication(@NotNull Context context, String packageName) throws PackageManager.NameNotFoundException{
        Drawable iconApp = context.getPackageManager().getApplicationIcon(packageName);
        return iconApp;
    }

    public static String getdNameApplication(@NotNull Context context, String packageName) throws PackageManager.NameNotFoundException{
        PackageManager packageManager = context.getPackageManager();
        String nameApp = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        return nameApp;
    }

    public static void startActivityAllStage(Context context, Intent intent) {
        if (KeepLiveAccessibilityService.getInstance() != null)
            context = KeepLiveAccessibilityService.getInstance();
        try {
            PendingIntent.getActivity(context, (int) (Math.random() * 9999.0d), intent, PendingIntent.FLAG_UPDATE_CURRENT).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            context.startActivity(intent);
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
        int w = 20;
        int h = 20;
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        int[] pixex = new int[w * h];
        List<Integer> trIndexs = new ArrayList<Integer>();
        for (int i = 0; i < bitmap.getHeight(); i++) {
            for (int j = 0; j < bitmap.getWidth(); j++) {
                int color = bitmap.getPixel(j, i);
                int alpha = Color.alpha(color);
                if (alpha < 200) {
                    trIndexs.add(i * h + j);
                } else if (trIndexs.size() > 0) {
                    for (Integer tr : trIndexs) {
                        pixex[tr] = color;
                    }
                    trIndexs.clear();
                    pixex[i * h + j] = color;
                } else {
                    pixex[i * h + j] = color;
                }
            }
        }

        Bitmap bitmap2 = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap2);
        RectF rectF = new RectF(0, 0,width,height);
        canvas2.drawBitmap(Bitmap.createBitmap(pixex, w, h, Bitmap.Config.ARGB_8888), null, rectF, null);
        return bitmap2;
    }

}
