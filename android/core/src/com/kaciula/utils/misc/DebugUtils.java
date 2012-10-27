
package com.kaciula.utils.misc;

import java.text.DecimalFormat;

import org.apache.http.Header;
import org.apache.http.message.AbstractHttpMessage;

import android.os.Debug;

import com.makanstudios.roadalert.BuildConfig;

/**
 * Common utilities when trying to debug memory issues
 * 
 * @author ka
 */
public class DebugUtils {

    private static final String TAG = "DebugUtils";

    public static void logHeap(String message) {
        if (BuildConfig.DEBUG) {
            Double allocated = Double.valueOf(Debug.getNativeHeapAllocatedSize())
                    / Double.valueOf((1048576));
            Double available = Double.valueOf(Debug.getNativeHeapSize() / 1048576.0);
            Double free = Double.valueOf(Debug.getNativeHeapFreeSize() / 1048576.0);
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(2);

            LogUtils.d("------" + message + "----");
            LogUtils.d(
                    TAG,
                    "debug.heap native: allocated " + df.format(allocated) + "MB of "
                            + df.format(available) + "MB (" + df.format(free) + "MB free)");
            LogUtils.d(
                    TAG,
                    "debug.memory: allocated: "
                            + df.format(Double
                                    .valueOf(Runtime.getRuntime().totalMemory() / 1048576))
                            + "MB of "
                            + df.format(Double.valueOf(Runtime.getRuntime().maxMemory() / 1048576))
                            + "MB ("
                            + df.format(Double.valueOf(Runtime.getRuntime().freeMemory() / 1048576))
                            + "MB free)");
            System.gc();
            System.gc();
        }
    }

    public static void logHeaders(AbstractHttpMessage method) {
        if (BuildConfig.DEBUG) {
            Header[] headers = method.getAllHeaders();

            LogUtils.d(TAG, "Headers:");
            for (Header header : headers) {
                LogUtils.d(TAG, header.toString());
            }
        }
    }
}
