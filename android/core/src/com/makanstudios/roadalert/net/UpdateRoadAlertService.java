
package com.makanstudios.roadalert.net;

import android.content.Intent;

import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.net.ApiService;
import com.kaciula.utils.net.ServiceException;
import com.kaciula.utils.net.ServiceReturnInfo;
import com.makanstudios.roadalert.model.Alert;
import com.makanstudios.roadalert.utils.GlobalUtils;
import com.makanstudios.roadalert.utils.NotificationUtils;

public class UpdateRoadAlertService extends ApiService {

    private static final String TAG = LogUtils.makeLogTag(UpdateRoadAlertService.class
            .getSimpleName());

    public UpdateRoadAlertService() {
        super(TAG);
    }

    @Override
    protected ServiceReturnInfo onRequest(Intent intent) {
        ServiceReturnInfo retInfo = new ServiceReturnInfo();

        try {
            LogUtils.d("Get alerts " + GlobalUtils.getLatestSync());

            Alert[] alerts = NetService.getInstance().getAlerts(GlobalUtils.getLatestSync());
            DatabaseHandler.populateAlerts(alerts);
            GlobalUtils.saveLatestSync();
            GlobalUtils.saveFirstTime(false);

            NotificationUtils.showNotification();

            retInfo.success = true;
        } catch (ServiceException se) {
            LogUtils.printStackTrace(se);
            retInfo.success = false;
        }

        return retInfo;
    }
}
