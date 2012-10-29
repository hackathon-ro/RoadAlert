
package com.makanstudios.roadalert.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.codehaus.jackson.map.ObjectMapper;

import android.text.TextUtils;
import android.util.Base64;

import com.google.android.gcm.GCMRegistrar;
import com.kaciula.utils.components.PlatformSpecificFactory;
import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.misc.MiscConstants;
import com.kaciula.utils.net.IWebService;
import com.kaciula.utils.net.ServiceException;
import com.kaciula.utils.ui.BasicApplication;
import com.makanstudios.roadalert.R;
import com.makanstudios.roadalert.model.Alert;
import com.makanstudios.roadalert.utils.Config;

public class NetService {

    private static final String TAG = LogUtils.makeLogTag(NetService.class.getSimpleName());

    private static NetService instance;

    private IWebService service;

    private ObjectMapper mapper;

    private static final int MAX_ATTEMPTS = 5;

    private static final int BACKOFF_MILLI_SECONDS = 2000;

    private static final Random random = new Random();

    private static final String URL_GCM_REGISTER = Config.GCM_SERVER_URL + "/register";

    private static final String URL_GCM_UNREGISTER = Config.GCM_SERVER_URL + "/unregister";

    private static final String URL_API_ALERTS = Config.API_PATH + "/alerts";

    private NetService() {
        service = PlatformSpecificFactory.getWebService(R.raw.api_keystore,
                Config.API_KEYSTORE_TYPE, Config.API_KEYSTORE_PASS, 0);
        mapper = new ObjectMapper();
    }

    public static NetService getInstance() {
        if (instance == null)
            instance = new NetService();

        return instance;
    }

    public Alert[] getAlerts(long latest) throws ServiceException {
        Map<String, String> params = null;
        params = new HashMap<String, String>();
        params.put("timestamp", "" + latest);

        Map<String, String> headers = getHeaders();
        try {
            String body = service.get(URL_API_ALERTS, headers, params);
            if (!TextUtils.isEmpty(body)) {
                Alert[] alerts = mapper.readValue(body, Alert[].class);
                return alerts;
            }
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
        }

        throw new ServiceException();
    }

    public void addAlert(Alert alert) throws ServiceException {

        Map<String, String> headers = getHeaders();
        try {
            String body = mapper.writeValueAsString(alert);
            service.post(URL_API_ALERTS, headers, null, body);
            return;
        } catch (Exception e) {
            LogUtils.printStackTrace(e);
        }

        throw new ServiceException();
    }

    public void deleteAllAlerts() throws ServiceException {
        Map<String, String> headers = getHeaders();
        try {
            service.delete(URL_API_ALERTS, headers, null);
            return;
        } catch (ServiceException se) {
            LogUtils.printStackTrace(se);
        }

        throw new ServiceException();
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

        Map<String, String> headers = getHeaders();

        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            LogUtils.d(TAG, "Attempt #" + i + " to register");
            try {
                service.get(URL_GCM_REGISTER, headers, params);
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

        Map<String, String> headers = getHeaders();

        try {
            service.get(URL_GCM_UNREGISTER, headers, params);
            GCMRegistrar.setRegisteredOnServer(BasicApplication.getContext(), false);
        } catch (ServiceException se) {
            LogUtils.printStackTrace(se);
        }
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MiscConstants.NET_HEADER_CONTENT_TYPE,
                MiscConstants.NET_HEADER_CONTENT_TYPE_WWW);
        String auth = Base64.encodeToString(
                (Config.API_USERNAME + ":" + Config.API_PASSWORD).getBytes(),
                Base64.URL_SAFE | Base64.NO_WRAP);
        headers.put(MiscConstants.NET_HEADER_AUTHORIZATION, "Basic " + auth);
        headers.put(MiscConstants.NET_HEADER_CONTENT_TYPE,
                MiscConstants.NET_HEADER_CONTENT_TYPE_JSON);
        headers.put(MiscConstants.NET_HEADER_ACCEPT,
                MiscConstants.NET_TYPE_APPLICATION_JSON);
        return headers;
    }
}
