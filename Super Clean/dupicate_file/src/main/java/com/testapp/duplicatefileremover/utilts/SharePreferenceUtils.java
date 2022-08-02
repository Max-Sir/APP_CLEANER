package com.testapp.duplicatefileremover.utilts;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtils {
    private static SharePreferenceUtils instance;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private SharedPreferences pre;

    private SharePreferenceUtils(Context context) {
        this.mContext = context;
        this.pre = context.getSharedPreferences("data_app", Context.MODE_PRIVATE);
        this.editor = this.pre.edit();
    }

    public static SharePreferenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreferenceUtils(context);
        }
        return instance;
    }
    public boolean getFirstRun() {
        boolean isFirst = pre.getBoolean("first_run_app", true);
        if (isFirst) {
            editor.putBoolean("first_run_app", false);
            editor.commit();
        }
        return isFirst;
    }
    public String getLanguage() {
        return pre.getString("language", "en");
    }

    public void saveLanguage(String language) {
        editor.putString("language", language);
        editor.commit();
    }
    public int getLanguageIndex() {
        return pre.getInt("languageindex", 2);
    }

    public void saveLanguageIndex(int language) {
        editor.putInt("languageindex", language);
        editor.commit();
    }

}
