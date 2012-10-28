
package com.makanstudios.roadalert.provider;

import android.provider.BaseColumns;

import com.makanstudios.roadalert.provider.RoadAlertContract.Alerts;

public interface AlertsQuery {

    String[] PROJECTION = {
            BaseColumns._ID,
            Alerts.ALERT_ID,
            Alerts.LAT,
            Alerts.LON,
            Alerts.TIMESTAMP,
            Alerts.NOTIFIED
    };

    int ALERT_ID = 1;

    int LAT = 2;

    int LON = 3;

    int TIMESTAMP = 4;

    int NOTIFIED = 5;
}
