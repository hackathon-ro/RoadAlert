
package com.makanstudios.roadalert.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;
import com.makanstudios.roadalert.ui.misc.RoadAlertApplication;

public class LocationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LocationBroadcastReceiver", "onReceive: received location update");

        final LocationInfo locationInfo = (LocationInfo) intent
                .getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);

        RoadAlertApplication.currentLocationData.setCurrentLocation(locationInfo);
        NotificationUtils.showNotification();
    }
}
