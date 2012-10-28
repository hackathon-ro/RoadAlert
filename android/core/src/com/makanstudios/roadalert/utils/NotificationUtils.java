
package com.makanstudios.roadalert.utils;

import org.joda.time.DateTime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;

import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.ui.BasicApplication;
import com.makanstudios.roadalert.R;
import com.makanstudios.roadalert.net.DatabaseHandler;
import com.makanstudios.roadalert.provider.AlertsQuery;
import com.makanstudios.roadalert.ui.activity.MainActivity;

public class NotificationUtils {

    public static void showNotification() {
        LogUtils.d("If necessary, show notification with alert");
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
        /*
         * Cursor cursor = DatabaseHandler.getTodayCursor(); if (cursor != null)
         * { if (cursor.moveToFirst()) {
         */
        Context ctx = BasicApplication.getContext();
        NotificationCompat.Builder nb = new NotificationCompat.Builder(ctx);
        nb.setContentTitle(ctx.getString(R.string.alert_ahead));
        nb.setContentText(ctx.getString(R.string.alert_ahead));

        nb.setSmallIcon(R.drawable.ic_launcher);
        nb.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
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
        /*
         * } cursor.close(); }
         */
    }
}
