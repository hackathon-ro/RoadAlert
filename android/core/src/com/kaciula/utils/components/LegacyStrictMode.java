
package com.kaciula.utils.components;

import android.annotation.TargetApi;
import android.os.StrictMode;

/**
 * Implementation that supports the Strict Mode functionality available for the
 * first platform release that supported Strict Mode.
 */
public class LegacyStrictMode implements IStrictMode {

    @TargetApi(9)
    public void enableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }
}
