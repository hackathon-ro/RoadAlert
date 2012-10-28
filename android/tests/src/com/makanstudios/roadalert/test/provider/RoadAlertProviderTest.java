
package com.makanstudios.roadalert.test.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.kaciula.utils.misc.LogUtils;
import com.kaciula.utils.provider.UriHandler;
import com.makanstudios.roadalert.model.Alert;
import com.makanstudios.roadalert.net.DatabaseHandler;
import com.makanstudios.roadalert.provider.AlertsQuery;
import com.makanstudios.roadalert.provider.RoadAlertContract;
import com.makanstudios.roadalert.provider.RoadAlertContract.Alerts;
import com.makanstudios.roadalert.provider.RoadAlertProvider;

public class RoadAlertProviderTest extends ProviderTestCase2<RoadAlertProvider> {

    private static final String TAG = LogUtils.makeLogTag(RoadAlertProviderTest.class
            .getSimpleName());

    private Context ctx;

    private MockContentResolver resolver;

    private static final long[][] alertData = {
            {
                    1, 55755800, 37617600
            }, {
                    2, 59332800, 18064500
            }, {
                    3, 59939000, 30315800
            }, {
                    4, 60169800, 24938200
            }, {
                    5, 60451400, 22268700
            }, {
                    6, 59438900, 24754500
            }, {
                    7, 66498700, 25721100
            }
    };

    public RoadAlertProviderTest() {
        super(RoadAlertProvider.class, RoadAlertContract.CONTENT_AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ctx = getMockContext();
        assertNotNull(ctx);

        resolver = getMockContentResolver();
        assertNotNull(resolver);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInsertReplaceAlert() {
        long id = alertData[0][0];
        long lat = alertData[0][1];
        long lon = alertData[0][2];
        Uri uri = insertAlert(new Alert(id, lat, lon));

        Cursor cursor = resolver.query(uri, AlertsQuery.PROJECTION, null,
                null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(id, cursor.getLong(AlertsQuery.ALERT_ID));
        assertEquals(lat, cursor.getLong(AlertsQuery.LAT));
        assertEquals(lon, cursor.getLong(AlertsQuery.LON));
        cursor.close();

        long newLat = alertData[1][1];
        long newLon = alertData[1][2];
        Uri newUri = insertAlert(new Alert(id, newLat, newLon));
        assertEquals(uri, newUri);

        cursor = resolver.query(uri, AlertsQuery.PROJECTION, null,
                null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(newLat, cursor.getLong(AlertsQuery.LAT));
        assertEquals(newLon, cursor.getLong(AlertsQuery.LON));
        assertEquals(false, cursor.getInt(AlertsQuery.NOTIFIED) == 1 ? true : false);
        cursor.close();

        DatabaseHandler.updateAlertNotified(Long.parseLong(UriHandler.getId(newUri)));

        cursor = resolver.query(uri, AlertsQuery.PROJECTION, null,
                null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals(newLat, cursor.getLong(AlertsQuery.LAT));
        assertEquals(newLon, cursor.getLong(AlertsQuery.LON));
        assertEquals(true, cursor.getInt(AlertsQuery.NOTIFIED) == 1 ? true : false);
        cursor.close();
    }

    private Uri insertAlert(Alert alert) {
        ContentValues values = new ContentValues();
        values.put(Alerts.ALERT_ID, alert.id);
        values.put(Alerts.LAT, alert.lat);
        values.put(Alerts.LON, alert.lon);
        values.put(Alerts.TIMESTAMP, alert.timestamp);
        values.put(Alerts.DEVICE_ID, alert.deviceId);

        Uri uri = resolver.insert(Alerts.CONTENT_URI, values);
        return uri;
    }
}
