
package com.makanstudios.roadalert.net;

import android.content.Intent;
import android.database.Cursor;

import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.net.ApiService;
import com.kaciula.utils.net.ServiceException;
import com.kaciula.utils.net.ServiceReturnInfo;
import com.makanstudios.roadalert.model.Alert;
import com.makanstudios.roadalert.provider.AlertsQuery;
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
            LogUtils.d("Get alerts");
            Alert[] alerts = NetService.getInstance().getAlerts();
            DatabaseHandler.populateAlerts(alerts);

            Cursor cursor = DatabaseHandler.getAlerts();
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        LogUtils.d("id: " + cursor.getLong(AlertsQuery.ALERT_ID));
                        LogUtils.d("lat: " + cursor.getLong(AlertsQuery.LAT));
                        LogUtils.d("lon: " + cursor.getLong(AlertsQuery.LON));
                        LogUtils.d("timestamp: " + cursor.getLong(AlertsQuery.TIMESTAMP));
                        LogUtils.d("-----");
                    }
                } finally {
                    cursor.close();
                }
            }

            // TODO: put alert id here
            int alertId = 2;
            NotificationUtils.showNotification(alertId);

            retInfo.success = true;
        } catch (ServiceException se) {
            LogUtils.printStackTrace(se);
            retInfo.success = false;
        }

        return retInfo;
    }
}
