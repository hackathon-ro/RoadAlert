
package com.makanstudios.roadalert.test.api;

import java.io.File;
import java.security.Security;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import junit.framework.TestCase;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.kaciula.utils.net.ServiceException;
import com.makanstudios.roadalert.model.Alert;
import com.makanstudios.roadalert.net.NetService;
import com.makanstudios.roadalert.utils.CustomConstants;

public class Api extends TestCase {

    private static final long[][] alertData = {
            {
                    55755800, 37617600
            }, {
                    59332800, 18064500
            }, {
                    59939000, 30315800
            }, {
                    60169800, 24938200
            }, {
                    60451400, 22268700
            }, {
                    59438900, 24754500
            }, {
                    66498700, 25721100
            }
    };

    public void setUp() throws Exception {
        initApi();
        cleanupDb();
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
            Alert[] alerts = NetService.getInstance().getAlerts();
            assertNotNull(alerts);
            assertEquals(0, alerts.length);

            for (int i = 0; i < alertData.length; i++) {
                createAlert(alertData[i][0], alertData[i][1]);
            }

            alerts = NetService.getInstance().getAlerts();
            assertNotNull(alerts);
            assertEquals(alertData.length, alerts.length);
        } catch (ServiceException se) {
            fail("Error talking with server");
        }
    }

    private Alert createAlert(long lat, long lon) {
        Alert alert;
        try {
            alert = new Alert(lat, lon);
            NetService.getInstance().addAlert(alert);
        } catch (ServiceException se) {
            fail("Could not create alert on the server");
            return null;
        }

        return alert;
    }

    private void initApi() {
        Security.addProvider(new BouncyCastleProvider());

        File file = new File("keystore_makan_studios.bks");
        HttpsURLConnection.setDefaultSSLSocketFactory(NetUtils.getFactory(file,
                CustomConstants.API_KEYSTORE_TYPE,
                CustomConstants.API_KEYSTORE_PASS));

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }
}
