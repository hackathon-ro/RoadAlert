
package com.makanstudios.roadalert.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class RoadAlertContract {

    public static final String CONTENT_AUTHORITY = "com.makanstudios.roadalert.provider";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    interface AlertColumns {

        String ALERT_ID = "alert_id";

        String LAT = "lat";

        String LON = "lon";

        String TIMESTAMP = "timestamp";

        String NOTIFIED = "notified";
    }

    static final String PATH_ALERTS = "alerts";

    public static class Alerts implements AlertColumns, BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_ALERTS)
                .build();

        public static final String DEFAULT_SORT_ORDER = BaseColumns._ID + " ASC";
    }

    public static class UriHandler {

        public static Uri buildUri(Uri contentUri, long id) {
            return contentUri.buildUpon().appendPath("" + id).build();
        }

        public static Uri buildUri(Uri contentUri, String id) {
            return contentUri.buildUpon().appendPath(id).build();
        }

        public static String getId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
