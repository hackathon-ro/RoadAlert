
package com.makanstudios.roadalert.test.api;

import android.content.Context;
import android.test.AndroidTestCase;

import com.kaciula.utils.net.ServiceException;
import com.kaciula.utils.ui.BasicApplication;
import com.makanstudios.roadalert.model.Alert;
import com.makanstudios.roadalert.net.NetService;

public class Api extends AndroidTestCase {

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

    private Context ctx;

    public void setUp() throws Exception {
        ctx = BasicApplication.getContext();
        // cleanupDb();
    }

    public void tearDown() throws Exception {
    }

    private void cleanupDb() {
        try {
            NetService.getInstance().deleteAllAlerts();
        } catch (ServiceException se) {
        }
    }

    public void testAddAlert() {
        try {
            Alert[] alerts = NetService.getInstance().getAlerts(0);
            assertNotNull(alerts);
            assertEquals(0, alerts.length);

            for (int i = 0; i < alertData.length; i++) {
                createAlert(alertData[i][0], alertData[i][1], alertData[i][2]);
            }

            alerts = NetService.getInstance().getAlerts(0);
            assertNotNull(alerts);
            assertEquals(alertData.length, alerts.length);
        } catch (ServiceException se) {
            fail("Error talking with server");
        }
    }

    private Alert createAlert(long id, long lat, long lon) {
        Alert alert;
        try {
            alert = new Alert(id, lat, lon);
            NetService.getInstance().addAlert(alert);
        } catch (ServiceException se) {
            fail("Could not create alert on the server");
            return null;
        }

        return alert;
    }
}
