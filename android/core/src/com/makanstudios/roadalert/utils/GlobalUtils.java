
package com.makanstudios.roadalert.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.kaciula.utils.components.PlatformSpecificFactory;
import com.kaciula.utils.ui.BasicApplication;

@SuppressLint("CommitPrefEdits")
public class GlobalUtils {

    public static String getDeviceId() {
        return android.provider.Settings.Secure.getString(BasicApplication.getContext()
                .getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    public static long getLatestSync() {
        SharedPreferences prefs = BasicApplication.getContext().getSharedPreferences(Prefs.GLOBAL,
                Context.MODE_PRIVATE);
        return prefs.getLong(Prefs.LATEST_SYNC, Constants.TIMESTAMP_ALL);
    }

    public static void saveLatestSync() {
        SharedPreferences prefs = BasicApplication.getContext().getSharedPreferences(Prefs.GLOBAL,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(Prefs.LATEST_SYNC, new DateTime(DateTimeZone.UTC).getMillis());
        PlatformSpecificFactory.getSharedPreferenceSaver().savePreferences(editor);
    }

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

        String LATEST_SYNC = "latest_sync";
    }
}
