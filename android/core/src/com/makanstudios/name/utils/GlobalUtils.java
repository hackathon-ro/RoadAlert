
package com.makanstudios.name.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.kaciula.utils.components.PlatformSpecificFactory;
import com.kaciula.utils.ui.BasicApplication;

@SuppressLint("CommitPrefEdits")
public class GlobalUtils {

    public static boolean isFirstTime() {
        SharedPreferences prefs = BasicApplication.getContext().getSharedPreferences(Prefs.GLOBAL,
                Context.MODE_PRIVATE);
        return prefs.getBoolean(Prefs.IS_FIRST_TIME, true);
    }

    public static void saveFirstTime(boolean isFirstTime) {
        SharedPreferences prefs = BasicApplication.getContext().getSharedPreferences(Prefs.GLOBAL,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Prefs.IS_FIRST_TIME, isFirstTime);
        PlatformSpecificFactory.getSharedPreferenceSaver().savePreferences(editor);
    }

    public static int getPreviousVersionCode() {
        SharedPreferences prefs = BasicApplication.getContext().getSharedPreferences(Prefs.GLOBAL,
                Context.MODE_PRIVATE);
        return prefs.getInt(Prefs.PREVIOUS_VERSION_CODE, 0);
    }

    public static void saveVersionCode(int versionCode) {
        SharedPreferences prefs = BasicApplication.getContext().getSharedPreferences(Prefs.GLOBAL,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Prefs.PREVIOUS_VERSION_CODE, versionCode);
        PlatformSpecificFactory.getSharedPreferenceSaver().savePreferences(editor);
    }

    private interface Prefs {

        String GLOBAL = "prefs_global";

        String IS_FIRST_TIME = "is_first_time";

        String PREVIOUS_VERSION_CODE = "previous_version_code";
    }
}
