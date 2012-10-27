
package com.kaciula.utils.net;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.kaciula.utils.components.PlatformSpecificFactory;
import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.misc.NetUtils;

public abstract class UpdateService extends IntentService {

    // Open this in child classes
    public static SharedPreferences prefs;

    public static Editor prefsEditor;

    protected long UPDATE_MAX_TIME;

    public UpdateService(String tag) {
        super(tag);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent
                .getParcelableExtra(ServiceConstants.EXTRA_STATUS_RECEIVER);
        if (receiver != null)
            receiver.send(ServiceConstants.STATUS_RUNNING, Bundle.EMPTY);

        ServiceReturnInfo retInfo = null;

        if (NetUtils.isInternetAvailable()) {
            // If we are connected check to see if this is a forced update
            boolean doUpdate = intent.getBooleanExtra(ServiceConstants.EXTRA_FORCE_REFRESH, false);

            if (!doUpdate) {
                LogUtils.d("No forced update");

                // Retrieve the last update time
                long lastTime = prefs.getLong(Prefs.LAST_UPDATE_TIME, Long.MIN_VALUE);

                // If update time has been passed, do an update
                if ((lastTime < System.currentTimeMillis() - UPDATE_MAX_TIME))
                    doUpdate = true;
            }

            if (doUpdate) {
                retInfo = onUpdate(intent);

                if (retInfo.success) {
                    prefsEditor.putLong(Prefs.LAST_UPDATE_TIME, System.currentTimeMillis());
                    PlatformSpecificFactory.getSharedPreferenceSaver().savePreferences(prefsEditor);
                }
            } else {
                retInfo = new ServiceReturnInfo();
                retInfo.success = true; // No need to do update then it's ok
            }
        } else
            retInfo = new ServiceReturnInfo();

        if (receiver != null) {
            if (retInfo.success) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ServiceConstants.EXTRA_RETURN_INFO, retInfo);

                receiver.send(ServiceConstants.STATUS_SUCCESS, bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ServiceConstants.EXTRA_RETURN_INFO, retInfo);

                receiver.send(ServiceConstants.STATUS_FAILURE, bundle);
            }
        }
    }

    protected abstract ServiceReturnInfo onUpdate(Intent intent);

    private interface Prefs {

        public static final String LAST_UPDATE_TIME = "last_update_time";
    }
}
