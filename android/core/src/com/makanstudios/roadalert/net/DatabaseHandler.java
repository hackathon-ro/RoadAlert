
package com.makanstudios.roadalert.net;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;

import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.provider.UriHandler;
import com.kaciula.utils.ui.BasicApplication;
import com.makanstudios.roadalert.model.Alert;
import com.makanstudios.roadalert.provider.AlertsQuery;
import com.makanstudios.roadalert.provider.RoadAlertContract;
import com.makanstudios.roadalert.provider.RoadAlertContract.Alerts;

public class DatabaseHandler {

    public static void populateAlerts(Alert[] alerts) {
        if (alerts == null || alerts.length == 0)
            return;

        Context ctx = BasicApplication.getContext();
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation.Builder builder = null;

        for (Alert alert : alerts) {
            ContentValues values = createAlertValues(alert);
            builder = ContentProviderOperation.newInsert(Alerts.CONTENT_URI);
            builder.withValues(values);
            batch.add(builder.build());
        }

        try {
            ctx.getContentResolver().applyBatch(RoadAlertContract.CONTENT_AUTHORITY, batch);
        } catch (RemoteException re) {
            LogUtils.printStackTrace(re);
        } catch (OperationApplicationException oae) {
            LogUtils.printStackTrace(oae);
        }
    }

    public static void insertAlert(Alert alert) {
        Context ctx = BasicApplication.getContext();
        ContentValues values = createAlertValues(alert);
        ctx.getContentResolver().insert(Alerts.CONTENT_URI, values);
    }

    public static void updateAlertNotified(long alertId) {
        Context ctx = BasicApplication.getContext();
        ContentValues values = new ContentValues();
        values.put(Alerts.NOTIFIED, true);
        ctx.getContentResolver().update(UriHandler.buildUri(Alerts.CONTENT_URI, alertId), values,
                null, null);
    }

    public static Cursor getAlerts() {
        Context ctx = BasicApplication.getContext();
        return ctx.getContentResolver().query(Alerts.CONTENT_URI, AlertsQuery.PROJECTION,
                null, null, null);
    }

    private static ContentValues createAlertValues(Alert alert) {
        ContentValues values = new ContentValues();
        values.put(Alerts.ALERT_ID, alert.id);
        values.put(Alerts.LAT, alert.lat);
        values.put(Alerts.LON, alert.lon);
        values.put(Alerts.TIMESTAMP, alert.timestamp);
        values.put(Alerts.DEVICE_ID, alert.deviceId);

        return values;
    }

}
