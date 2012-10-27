
package com.makanstudios.name.ui.misc;

import org.acra.ACRA;

import android.content.ComponentName;
import android.content.pm.PackageManager;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.misc.MiscUtils;
import com.kaciula.utils.ui.BasicApplication;
import com.kaciula.utils.ui.DialogUtils;
import com.makanstudios.name.BuildConfig;
import com.makanstudios.name.gcm.GCMIntentService;
import com.makanstudios.name.gcm.GCMRedirectedBroadcastReceiver;
import com.makanstudios.name.gcm.OnNewGcmNotificationReceiver;
import com.makanstudios.name.utils.AppParams;
import com.makanstudios.name.utils.GlobalUtils;

// FIXME: Setup crash reporting either with google docs or bugsense
// @ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=1b743149", formKey = "1b743149")
public class NameApplication extends BasicApplication {

    @Override
    public void onCreate() {
        if (!BuildConfig.DEBUG) {
            ACRA.init(this);
        }

        super.onCreate();

        init();
    }

    private void init() {
        DialogUtils.setMapping(DialogConstants.mapping);

        LogUtils.init("name_", false);

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

        // FIXME: Do we save these here or later?
        GlobalUtils.saveFirstTime(false);
        GlobalUtils.saveVersionCode(MiscUtils.getCurrentVersionCode());
    }
}
