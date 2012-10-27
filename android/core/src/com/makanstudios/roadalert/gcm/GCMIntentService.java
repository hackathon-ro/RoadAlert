
package com.makanstudios.roadalert.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.kaciula.utils.misc.LogUtils;
import com.makanstudios.roadalert.BuildConfig;
import com.makanstudios.roadalert.net.NetService;
import com.makanstudios.roadalert.utils.CustomConstants;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = LogUtils.makeLogTag(GCMIntentService.class.getSimpleName());

    private static final String GCM_MESSAGE_TYPE = "gcm_message_type";

    public GCMIntentService() {
        super(CustomConstants.GCM_SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        LogUtils.d(TAG, "Device registered: regId = " + registrationId);
        NetService.getInstance().registerGcm(registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        LogUtils.d(TAG, "Device unregistered");
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            NetService.getInstance().unregisterGcm(registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            LogUtils.d(TAG, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        LogUtils.d(TAG, "Received message");

        Bundle extras = intent.getExtras();
        if (extras == null)
            return;

        if (BuildConfig.DEBUG)
            for (String key : extras.keySet())
                LogUtils.d("key: " + key + " value: " + extras.getString(key));

        try {
            int type = Integer.parseInt(extras.getString(GCM_MESSAGE_TYPE));

            switch (type) {
                default:
                    return;
            }

            // sendOrderedBroadcast(new Intent(which_action), null);
        } catch (NumberFormatException nfe) {
        }

        // String message = getString(R.string.gcm_message);
        // displayMessage(context, message);
        // notifies user
        // generateNotification(context, message);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        LogUtils.d(TAG, "Received deleted messages notification");
        // String message = getString(R.string.gcm_deleted, total);
        // displayMessage(context, message);
        // notifies user
        // generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        LogUtils.d(TAG, "Received error: " + errorId);
        // displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        LogUtils.d(TAG, "Received recoverable error: " + errorId);
        // displayMessage(context, getString(R.string.gcm_recoverable_error,
        // errorId));
        return super.onRecoverableError(context, errorId);
    }
}
