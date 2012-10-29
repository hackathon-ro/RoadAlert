
package com.makanstudios.roadalert.utils;

public interface Constants {

    int TYPEFACE_ROBOTO_LIGHT = 0;

    int TYPEFACE_ROBOTO_REGULAR = 1;

    int TYPEFACE_ROBOTO_BOLD = 2;

    int TYPEFACE_ROBOTO_CONDENSED = 3;

    int TYPEFACE_DEFAULT = Constants.TYPEFACE_ROBOTO_REGULAR;

    int TIMESTAMP_ALL = 0;

    int NOTIF_MIN_MAX_DISTANCE = 5 * 1000; // 5 km

    int NOTIF_MIN_SPEED = 3; // minimum 10 km/h

    int NOTIF_TIME = 60 * 60 * 1000; // 1 hour

    int TRIGGER_SYNC_MAX_JITTER_MILLIS = 10;

    int LOCATION_FREQUENCY = 1 * 60 * 1000; // 1 min

    int LOCATION_MAX_AGE = 3 * 60 * 1000; // 3 mins
}
