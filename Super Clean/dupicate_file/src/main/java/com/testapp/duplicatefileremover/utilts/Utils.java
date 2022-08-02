package com.testapp.duplicatefileremover.utilts;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class Utils {
    public static String formatSize(long size) {
        if (size <= 0)
            return "";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    public static String getFileName(String  path) {
        String filename=path.substring(path.lastIndexOf("/")+1);
        return filename;
    }
    public static File[] getFileList(String str) {
        File file = new File(str);
        if (!file.isDirectory()) {
            return new File[0];
        }

        return file.listFiles();
    }
    public static boolean checkSelfPermission(Activity activity, String s) {
        if (isAndroid23()) {
            return ContextCompat.checkSelfPermission(activity, s) == 0;
        } else {
            return true;
        }
    }
    public static boolean isAndroid23() {
        return android.os.Build.VERSION.SDK_INT >=23;
    }

    public static void setLocale(Context mContext) {
        int index = SharePreferenceUtils.getInstance(mContext).getLanguageIndex();
        String language ="en";
        if(index==0){
            language ="cs";
        } else if(index==1){
            language ="de";
        }else if(index==2) {
            language = "en";
        }else if(index==3) {
            language = "es";
        }else if(index==4) {
            language = "fr";
        }else if(index==5) {
            language = "in";
        }else if(index==6) {
            language = "it";
        }else if(index==7) {
            language = "pl";
        }else if(index==8) {
            language = "pt";
        }else if(index==9) {
            language = "ru";
        }else if(index==10) {
            language = "tr";
        }else if(index==11) {
            language = "vi";
        }else if(index==12) {
            language = "ar";
        }else if(index==13) {
            language = "th";
        }else if(index==14) {
            language = "bn";
        }else if(index==15) {
            language = "hi";
        }else if(index==16) {
            language = "ta";
        }

        if (SharePreferenceUtils.getInstance(mContext).getFirstRun()) {
            language = Locale.getDefault().getLanguage();
            if (language.equalsIgnoreCase("cs")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(0);
            }
            if (language.equalsIgnoreCase("de")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(1);
            }
            if (language.equalsIgnoreCase("en")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(2);
            }
            if (language.equalsIgnoreCase("es")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(3);
            }
            if (language.equalsIgnoreCase("fr")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(4);
            }
            if (language.equalsIgnoreCase("in")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(5);
            }
            if (language.equalsIgnoreCase("it")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(6);
            }
            if (language.equalsIgnoreCase("pl")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(7);
            }
            if (language.equalsIgnoreCase("pt")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(8);
            }
            if (language.equalsIgnoreCase("ru")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(9);
            }
            if (language.equalsIgnoreCase("tr")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(10);
            }
            if (language.equalsIgnoreCase("vi")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(11);
            }
            if (language.equalsIgnoreCase("ar")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(12);
            }
            if (language.equalsIgnoreCase("th")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(13);
            }
            if (language.equalsIgnoreCase("bn")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(14);
            }
            if (language.equalsIgnoreCase("hi")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(15);
            }
            if (language.equalsIgnoreCase("ta")) {
                SharePreferenceUtils.getInstance(mContext).saveLanguageIndex(16);
            }


        }

        Locale myLocale = new Locale(language);
        Resources res = mContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

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

}
