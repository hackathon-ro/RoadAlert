
package com.makanstudios.name.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.util.Base64;

import com.google.android.gcm.GCMRegistrar;
import com.kaciula.utils.components.PlatformSpecificFactory;
import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.misc.MiscConstants;
import com.kaciula.utils.net.IWebService;
import com.kaciula.utils.net.ServiceException;
import com.kaciula.utils.ui.BasicApplication;
import com.makanstudios.name.R;
import com.makanstudios.name.utils.CustomConstants;

public class NetService {

    private static final String TAG = LogUtils.makeLogTag(NetService.class.getSimpleName());

    private static NetService instance;

    private IWebService service;

    private static final int MAX_ATTEMPTS = 5;

    private static final int BACKOFF_MILLI_SECONDS = 2000;

    private static final Random random = new Random();

    private static final String URL_GCM_REGISTER = CustomConstants.GCM_SERVER_URL + "/register";

    private static final String URL_GCM_UNREGISTER = CustomConstants.GCM_SERVER_URL + "/unregister";

    private NetService() {
        service = PlatformSpecificFactory.getWebService(R.raw.keystore_makan_studios,
                CustomConstants.API_KEYSTORE_TYPE, CustomConstants.API_KEYSTORE_PASS, 0);
    }

    public static NetService getInstance() {
        if (instance == null)
            instance = new NetService();

        return instance;
    }

    /**
     * Register this account/device pair within the server.
     * 
     * @return whether the registration succeeded or not.
     */
    public boolean registerGcm(final String regId) {
        LogUtils.d(TAG, "registering device (regId = " + regId + ")");

        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MiscConstants.NET_HEADER_CONTENT_TYPE,
                MiscConstants.NET_HEADER_CONTENT_TYPE_WWW);
        String auth = Base64.encodeToString(
                (CustomConstants.API_USERNAME + ":" + CustomConstants.API_PASSWORD).getBytes(),
                Base64.URL_SAFE | Base64.NO_WRAP);
        headers.put(MiscConstants.NET_HEADER_AUTHORIZATION, "Basic " + auth);

        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            LogUtils.d(TAG, "Attempt #" + i + " to register");
            try {
                service.post(URL_GCM_REGISTER, headers, params);
                GCMRegistrar.setRegisteredOnServer(BasicApplication.getContext(), true);
                return true;
            } catch (ServiceException se) {
                LogUtils.printStackTrace(se);

                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                LogUtils.e(TAG, "Failed to register on attempt " + i);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    LogUtils.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    LogUtils.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return false;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }

        return false;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public void unregisterGcm(final String regId) {
        LogUtils.d(TAG, "unregistering device (regId = " + regId + ")");

        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            service.post(URL_GCM_UNREGISTER, null, params);
            GCMRegistrar.setRegisteredOnServer(BasicApplication.getContext(), false);
        } catch (ServiceException se) {
            LogUtils.printStackTrace(se);
        }
    }
}
