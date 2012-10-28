
package com.makanstudios.roadalert.net;

import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import android.content.Intent;

import com.google.android.maps.GeoPoint;
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
            // TODO: Get location of alert
            Random random = new Random();
            int index = random.nextInt(sEurope.length);
            long lat = sEurope[index].getLatitudeE6();
            long lon = sEurope[index].getLongitudeE6();
            Alert alert = new Alert(0, lat, lon, new DateTime(DateTimeZone.UTC).getMillis());
            NetService.getInstance().addAlert(alert);
            retInfo.success = true;
        } catch (ServiceException se) {
            LogUtils.printStackTrace(se);
            retInfo.success = false;
        }

        return retInfo;
    }

    private static final GeoPoint[] sEurope = {
            new GeoPoint(55755800, 37617600),
            new GeoPoint(59332800, 18064500),
            new GeoPoint(59939000, 30315800),
            new GeoPoint(60169800, 24938200),
            new GeoPoint(60451400, 22268700),
            new GeoPoint(65584200, 22154700),
            new GeoPoint(59438900, 24754500),
            new GeoPoint(66498700, 25721100),
    };
}
