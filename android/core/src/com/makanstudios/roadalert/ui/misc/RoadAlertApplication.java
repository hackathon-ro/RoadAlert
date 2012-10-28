
package com.makanstudios.roadalert.ui.misc;

import org.acra.ACRA;

import android.content.ComponentName;
import android.content.pm.PackageManager;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.misc.MiscUtils;
import com.kaciula.utils.ui.BasicApplication;
import com.kaciula.utils.ui.DialogUtils;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.makanstudios.roadalert.BuildConfig;
import com.makanstudios.roadalert.gcm.GCMIntentService;
import com.makanstudios.roadalert.gcm.GCMRedirectedBroadcastReceiver;
import com.makanstudios.roadalert.gcm.OnNewGcmNotificationReceiver;
import com.makanstudios.roadalert.utils.AppParams;
import com.makanstudios.roadalert.utils.CustomConstants;
import com.makanstudios.roadalert.utils.GlobalUtils;

// FIXME: Setup crash reporting either with google docs or bugsense
// @ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=1b743149", formKey = "1b743149")
public class RoadAlertApplication extends BasicApplication {

    public static LocationInfo currentLocation;

    @Override
    public void onCreate() {
        if (!BuildConfig.DEBUG) {
            ACRA.init(this);
        }

        super.onCreate();

        init();
    }

    private void init() {
        LocationLibrary.showDebugOutput(BuildConfig.DEBUG);
        LocationLibrary.initialiseLibrary(getBaseContext(), CustomConstants.LOCATION_FREQUENCY,
                CustomConstants.LOCATION_MAX_AGE, getPackageName());

        DialogUtils.setMapping(DialogConstants.mapping);

        LogUtils.init("roadalert_", false);

        /* GlobalUtils.saveVersionCode(MiscUtils.getCurrentVersionCode()); */

        GoogleAnalytics.getInstance(this).setAppOptOut(BuildConfig.DEBUG);
        EasyTracker.getInstance().setContext(this);

        AppParams.init(this);

        if (!AppParams.hasGcmSupport) {
            PackageManager pm = BasicApplication.getContext().getPackageManager();
            pm.setComponentEnabledSetting(new ComponentName(BasicApplication.getContext(),
                    GCMIntentService.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(new ComponentName(BasicApplication.getContext(),
                    OnNewGcmNotificationReceiver.class),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(new ComponentName(BasicApplication.getContext(),
                    GCMRedirectedBroadcastReceiver.class),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }

        GlobalUtils.saveVersionCode(MiscUtils.getCurrentVersionCode());
    }
}
