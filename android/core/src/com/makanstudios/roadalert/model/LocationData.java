
package com.makanstudios.roadalert.model;

import android.location.Location;

import com.kaciula.utils.ui.BasicApplication;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.makanstudios.roadalert.utils.CustomConstants;

public class LocationData {

    public LocationInfo currentLocationInfo;

    public LocationInfo prevLocationInfo;

    public float avgSpeed;

    public LocationData() {
        currentLocationInfo = new LocationInfo(BasicApplication.getContext());
        // currentLocationInfo.lastLat = CustomConstants.LAT_RO;
        // currentLocationInfo.lastLong = CustomConstants.LON_RO;
        // currentLocationInfo.lastLocationUpdateTimestamp = 0;
    }

    public int getNotificationRadius() {
        return Math.max(CustomConstants.NOTIF_MIN_MAX_DISTANCE, (int) avgSpeed / 6);
    }

    public void setCurrentLocation(LocationInfo l) {
        if (prevLocationInfo == null) {
            avgSpeed = 0;
            prevLocationInfo = currentLocationInfo = l;
        } else {
            if (l.lastLocationUpdateTimestamp != prevLocationInfo.lastLocationUpdateTimestamp) {
                prevLocationInfo = currentLocationInfo;
                currentLocationInfo = l;

                Location prev = new Location("");
                prev.setLatitude(prevLocationInfo.lastLat);
                prev.setLongitude(prevLocationInfo.lastLong);

                Location curr = new Location("");
                curr.setLatitude(currentLocationInfo.lastLat);
                curr.setLongitude(currentLocationInfo.lastLong);

                float dist = curr.distanceTo(prev);
                long time = Math.abs(currentLocationInfo.lastLocationUpdateTimestamp
                        - prevLocationInfo.lastLocationUpdateTimestamp) / 1000;
                avgSpeed = dist / time;
            }
        }
    }

    public static float distanceBetweenLocations(double lat1, double lon1, double lat2, double lon2) {
        Location prev = new Location("");
        prev.setLatitude(lat1);
        prev.setLongitude(lon1);

        Location curr = new Location("");
        curr.setLatitude(lat2);
        curr.setLongitude(lon2);

        return curr.distanceTo(prev);
    }
}
