
package com.makanstudios.name.ui.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gcm.GCMRegistrar;
import com.kaciula.utils.misc.LogUtils;
import com.makanstudios.name.R;
import com.makanstudios.name.net.NetService;
import com.makanstudios.name.ui.misc.NameActivity;
import com.makanstudios.name.utils.AppParams;
import com.makanstudios.name.utils.CustomConstants;

public class DelegateActivity extends NameActivity {

    private AsyncTask<Void, Void, Void> mRegisterTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isTaskRoot()) {
            finish();
            return;
        }

        setContentView(R.layout.activity_delegate);

        doInitStuff();

        // FIXME: Decide which activity to start and then finish. The activities
    }

    @Override
    protected void onDestroy() {
        cleanInitStuff();
        super.onDestroy();
    }

    private void doInitStuff() {
        if (AppParams.hasGcmSupport)
            registerGcm();
    }

    private void cleanInitStuff() {
        if (AppParams.hasGcmSupport)
            cleanGcm();
    }

    private void registerGcm() {
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            GCMRegistrar.register(this, CustomConstants.GCM_SENDER_ID);
        } else {
            // Device is already registered on GCM, check server.
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                LogUtils.d("Already registered on GCM");
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        boolean registered = NetService.getInstance()
                                .registerGcm(regId);
                        // At this point all attempts to register with the app
                        // server failed, so we need to unregister the device
                        // from GCM - the app will try to register again when
                        // it is restarted. Note that GCM will send an
                        // unregistered callback upon completion, but
                        // GCMIntentService.onUnregistered() will ignore it.
                        if (!registered) {
                            GCMRegistrar.unregister(context);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    private void cleanGcm() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            GCMRegistrar.onDestroy(this);
        } catch (IllegalArgumentException iae) {
        }
    }
}
