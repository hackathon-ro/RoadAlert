
package com.makanstudios.roadalert.utils;

/*
 * These constants can be tweaked based on client's preferences or the developer's mood
 */
public interface CustomConstants {

    int DEVELOPER_SIGNATURE_DEBUG = 2136355075;

    int DEVELOPER_SIGNATURE_RELEASE = 1615067630;

    // FIXME: Replace GCM constants with the right ones
    String GCM_SENDER_ID = "839376984937";

    String GCM_SERVER_URL = "http://makan-studios.com:9080/gcm-server-rc-pro";

    String GCM_NEW_NOTIFICATION_INTENT = "com.makanstudios.roadalert.intent.GCM_NEW_NOTIFICATION";

    // FIXME: keystore different because it is public
    String API_KEYSTORE_TYPE = "BKS";

    String API_KEYSTORE_PASS = "Macaw987";

    String API_USERNAME = "roadalert";

    String API_PASSWORD = "Police";
}
