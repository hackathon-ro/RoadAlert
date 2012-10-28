
package com.makanstudios.roadalert.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.ui.BasicApplication;
import com.makanstudios.roadalert.R;
import com.makanstudios.roadalert.model.LocationData;
import com.makanstudios.roadalert.net.DatabaseHandler;
import com.makanstudios.roadalert.provider.AlertsQuery;
import com.makanstudios.roadalert.ui.activity.MainActivity;
import com.makanstudios.roadalert.ui.misc.RoadAlertApplication;

public class NotificationUtils {

    public static void showNotification() {
        LogUtils.d("If necessary, show notification with alert");
        Cursor cursor = DatabaseHandler.getAlerts();
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int alertId = (int) cursor.getLong(AlertsQuery.ALERT_ID);
                    int lat = (int) cursor.getLong(AlertsQuery.LAT);
                    int lon = (int) cursor.getLong(AlertsQuery.LON);
                    long timestamp = cursor.getLong(AlertsQuery.TIMESTAMP);
                    boolean notified = cursor.getInt(AlertsQuery.NOTIFIED) == 1 ? true : false;

                    if (notified) {
                        LogUtils.d("already notified");
                        continue;
                    }

                    LogUtils.d("lat: " + lat + " lon: " + lon + " time: " + timestamp);
                    DateTime now = new DateTime(DateTimeZone.UTC);
                    if ((now.getMillis() - timestamp) < CustomConstants.NOTIF_TIME) {
                        float distance = LocationData
                                .distanceBetweenLocations(
                                        ((double) lat) / 1E6,
                                        ((double) lon) / 1E6,
                                        RoadAlertApplication.currentLocationData.currentLocationInfo.lastLat,
                                        RoadAlertApplication.currentLocationData.currentLocationInfo.lastLong);
                        LogUtils.d("inside time frame / distance = " + distance + " m");

                        if (distance < RoadAlertApplication.currentLocationData
                                .getNotificationRadius()
                                && RoadAlertApplication.currentLocationData.avgSpeed >= CustomConstants.NOTIF_MIN_SPEED) {
                            LogUtils.d("inside distance frame");
                            popNotif(distance);
                            DatabaseHandler.updateAlertNotified(alertId);
                        }
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }

    private static void popNotif(float distance) {
        Context ctx = BasicApplication.getContext();
        NotificationCompat.Builder nb = new NotificationCompat.Builder(ctx);
        nb.setContentTitle(ctx.getString(R.string.alert_ahead));
        nb.setContentText(ctx.getString(R.string.alert_ahead));

        nb.setSmallIcon(R.drawable.ic_launcher);
        nb.setDefaults(Notification.DEFAULT_LIGHTS);
        nb.setSound(Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.raw.road_alert));
        nb.setAutoCancel(true);
        nb.setWhen(new DateTime().getMillis());

        Intent notifIntent = new Intent(ctx, MainActivity.class);
        notifIntent
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notifIntent, 0);
        nb.setContentIntent(contentIntent);

        NotificationManager nm = (NotificationManager) BasicApplication.getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, nb.build());
    }
}
