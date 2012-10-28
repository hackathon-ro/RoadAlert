
package com.makanstudios.roadalert.utils;

/*
 * These constants can be tweaked based on client's preferences or the developer's mood
 */
public interface CustomConstants {

    int DEVELOPER_SIGNATURE_DEBUG = 2136355075;

    int DEVELOPER_SIGNATURE_RELEASE = 1615067630;

    String API_ENDPOINT = "https://api.makan-studios.com:8443";

    String API_PATH = API_ENDPOINT + "/roadalert/v1";

    String GCM_SENDER_ID = "851909760106";

    String GCM_SERVER_URL = API_ENDPOINT + "/roadalert/gcm";

    String GCM_NEW_NOTIFICATION_INTENT = "com.makanstudios.roadalert.intent.GCM_NEW_NOTIFICATION";

    String API_KEYSTORE_TYPE = "BKS";

    String API_KEYSTORE_PASS = "Macaw987";

    String API_USERNAME = "roadalert";

    String API_PASSWORD = "Police";

    int TRIGGER_SYNC_MAX_JITTER_MILLIS = 3 * 1000; // 3 seconds

    int LOCATION_FREQUENCY = 60 * 1000; // 1 minute

    int LOCATION_MAX_AGE = 180 * 1000; // 3 minutes

    int NOTIF_TIME = 60 * 60 * 1000; // 1 hour

    float NOTIF_MIN_SPEED = 0; // this is normally something larger (2.7 m/s =
                               // 10 km/h); for
                               // hackathon
                               // purposes is 0

    float LAT_RO = 46.24825f;

    float LON_RO = 24.905182f;
}
