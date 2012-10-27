
package com.kaciula.utils.misc;

import android.util.Log;

import com.makanstudios.roadalert.BuildConfig;

/**
 * Wrapper for logging so that it is easy to disable logging on release builds.
 * Use this classs instead of Log directly.
 * 
 * @author ka
 */
public class LogUtils {

    private static String TAG;

    private static boolean USE_SAME_TAG_FOR_ENTIRE_APP;

    private static String LOG_PREFIX;

    private static int LOG_PREFIX_LENGTH;

    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    public static void init(String logPrefix, boolean useSameTag) {
        LOG_PREFIX = logPrefix;
        LOG_PREFIX_LENGTH = LOG_PREFIX.length();
        USE_SAME_TAG_FOR_ENTIRE_APP = useSameTag;
        TAG = makeLogTag(LogUtils.class.getSimpleName());
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            if (USE_SAME_TAG_FOR_ENTIRE_APP)
                Log.d(TAG, msg);
            else
                Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void printStackTrace(Throwable t) {
        if (BuildConfig.DEBUG)
            t.printStackTrace();
    }

    public static void throwRuntimeException(String message, Throwable t) {
        if (BuildConfig.DEBUG)
            throw new RuntimeException(message, t);
    }
}
