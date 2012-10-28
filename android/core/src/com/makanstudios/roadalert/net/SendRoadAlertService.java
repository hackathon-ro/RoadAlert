
package com.makanstudios.roadalert.net;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import android.content.Intent;

import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.net.ApiService;
import com.kaciula.utils.net.ServiceException;
import com.kaciula.utils.net.ServiceReturnInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.makanstudios.roadalert.model.Alert;
import com.makanstudios.roadalert.ui.misc.RoadAlertApplication;
import com.makanstudios.roadalert.utils.GlobalUtils;

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
            LocationInfo info = RoadAlertApplication.currentLocationData.currentLocationInfo;
            Alert alert = new Alert(0, (long) (info.lastLat * 1E6), (long) (info.lastLong * 1E6),
                    new DateTime(
                            DateTimeZone.UTC).getMillis(), GlobalUtils.getDeviceId());
            NetService.getInstance().addAlert(alert);
            retInfo.success = true;
        } catch (ServiceException se) {
            LogUtils.printStackTrace(se);
            retInfo.success = false;
        }

        return retInfo;
    }
}
