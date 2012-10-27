
package com.makanstudios.roadalert.net;

import android.content.Intent;

import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.net.ApiService;
import com.kaciula.utils.net.ServiceException;
import com.kaciula.utils.net.ServiceReturnInfo;
import com.makanstudios.roadalert.model.Alert;

public class SendRoadAlertService extends ApiService {

    private static final String TAG = LogUtils.makeLogTag(SendRoadAlertService.class
            .getSimpleName());

    public SendRoadAlertService() {
        super(TAG);
    }

    @Override
    protected ServiceReturnInfo onRequest(Intent intent) {
        ServiceReturnInfo retInfo = new ServiceReturnInfo();

        try {
            // TODO: Get location and time of alert
            Alert alert = new Alert(2345, 6789);
            NetService.getInstance().addAlert(alert);
            retInfo.success = true;
        } catch (ServiceException se) {
            LogUtils.printStackTrace(se);
            retInfo.success = false;
        }

        return retInfo;
    }
}
