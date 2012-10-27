
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
}
