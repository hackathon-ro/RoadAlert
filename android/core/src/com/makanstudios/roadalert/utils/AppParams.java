
package com.makanstudios.roadalert.utils;

import com.google.android.gcm.GCMRegistrar;
import com.kaciula.utils.misc.MiscUtils;
import com.kaciula.utils.ui.BasicApplication;

public class AppParams {

    public static boolean isForGooglePlay = true;

    public static boolean isKindleFire = false;

    public static boolean hasGcmSupport = true;

    public static void init(BasicApplication app) {
        isForGooglePlay = MiscUtils.isForGooglePlay(CustomConstants.DEVELOPER_SIGNATURE_DEBUG,
                CustomConstants.DEVELOPER_SIGNATURE_RELEASE);

        isKindleFire = MiscUtils.isKindleFire();

        try {
            GCMRegistrar.checkDevice(app);
            // GCMRegistrar.checkManifest(app);
            hasGcmSupport = true;
        } catch (UnsupportedOperationException uoe) {
            // The device does not have the google packages needed
            hasGcmSupport = false;
        }
    }
}
