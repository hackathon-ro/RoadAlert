/*
 * Copyright (C) 2012 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.makanstudios.roadalert.utils;

import android.util.Log;

import com.makanstudios.roadalert.BuildConfig;

/**
 * @author Cyril Mottier
 */
@SuppressWarnings("all")
public class Config {

    private static final String LOG_TAG = "Config";

    private Config() {
    }

    // /////////////////////////////////////////////////////////////
    //
    // Local config
    //
    // /////////////////////////////////////////////////////////////

    private static final String LOCAL_CONFIG_CLASS_NAME = "com.makanstudios.roadalert.utils.LocalConfig";
    private static final String LOCAL_GOOGLE_MAPS_API_KEY_RELEASE_FIELD_NAME = "GOOGLE_MAPS_API_KEY_RELEASE";
    private static final String LOCAL_GOOGLE_MAPS_API_KEY_DEBUG_FIELD_NAME = "GOOGLE_MAPS_API_KEY_DEBUG";
    private static final String LOCAL_DEVELOPER_SIGNATURE_DEBUG = "DEVELOPER_SIGNATURE_DEBUG";
    private static final String LOCAL_DEVELOPER_SIGNATURE_RELEASE = "DEVELOPER_SIGNATURE_RELEASE";
    private static final String LOCAL_API_ENDPOINT = "API_ENDPOINT";
    private static final String LOCAL_API_PATH = "API_PATH";
    private static final String LOCAL_GCM_SENDER_ID = "GCM_SENDER_ID";
    private static final String LOCAL_GCM_SERVER_URL = "GCM_SERVER_URL";
    private static final String LOCAL_GCM_NEW_NOTIFICATION_INTENT = "GCM_NEW_NOTIFICATION_INTENT";
    private static final String LOCAL_API_KEYSTORE_TYPE = "API_KEYSTORE_TYPE";
    private static final String LOCAL_API_KEYSTORE_PASS = "API_KEYSTORE_PASS";
    private static final String LOCAL_API_USERNAME = "API_USERNAME";
    private static final String LOCAL_API_PASSWORD = "API_PASSWORD";

    private static final Class<?> LOCAL_CONFIG_CLASS = getLocalConfig();

    private static Class<?> getLocalConfig() {
        try {
            return Class.forName(LOCAL_CONFIG_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, "No local configuration file with class name ("
                    + LOCAL_CONFIG_CLASS_NAME + ") found");
            return null;
        }
    }

    /**
     * Uses reflection in order to extract a local configuration.
     * 
     * @return The String associated to the given name (should be stored as a
     *         static final variable) or the defaultValue if an error occurred.
     */
    private static String getLocalConfigString(String name) {
        try {
            return (String) LOCAL_CONFIG_CLASS.getField(name).get(null);
        } catch (Exception e) {
            Log.e(LOG_TAG, "No local configuration found with key " + name);
            return null;
        }
    }

    /**
     * Uses reflection in order to extract a local configuration.
     * 
     * @return The int associated to the given name (should be stored as a
     *         static final variable) or the defaultValue if an error occurred.
     */
    private static int getLocalConfigInt(String name) {
        try {
            return (Integer) LOCAL_CONFIG_CLASS.getField(name).get(null);
        } catch (Exception e) {
            Log.e(LOG_TAG, "No local configuration found with key " + name);
            return 0;
        }
    }

    // /////////////////////////////////////////////////////////////
    //
    // Compilation target
    //
    // /////////////////////////////////////////////////////////////

    private static final int COMPILATION_TARGET_RELEASE = 0;
    private static final int COMPILATION_TARGET_DEBUG = 1;

    /**
     * The current compilation target
     */
    private static final int COMPILATION_TARGET = BuildConfig.DEBUG ? COMPILATION_TARGET_DEBUG
            : COMPILATION_TARGET_RELEASE;

    // /////////////////////////////////////////////////////////////
    //
    // Google Maps API key
    //
    // /////////////////////////////////////////////////////////////

    /**
     * The current Google Maps API key to use when using MapView instances
     */
    public static final String GOOGLE_MAPS_API_KEY;

    public static final int DEVELOPER_SIGNATURE_DEBUG;

    public static final int DEVELOPER_SIGNATURE_RELEASE;

    public static final String API_ENDPOINT;

    public static final String API_PATH;

    public static final String GCM_SENDER_ID;

    public static final String GCM_SERVER_URL;

    public static final String GCM_NEW_NOTIFICATION_INTENT;

    public static final String API_KEYSTORE_TYPE;

    public static final String API_KEYSTORE_PASS;

    public static final String API_USERNAME;

    public static final String API_PASSWORD;

    static {
        GOOGLE_MAPS_API_KEY = (COMPILATION_TARGET == COMPILATION_TARGET_RELEASE) ? getLocalConfigString(LOCAL_GOOGLE_MAPS_API_KEY_RELEASE_FIELD_NAME)
                : getLocalConfigString(LOCAL_GOOGLE_MAPS_API_KEY_DEBUG_FIELD_NAME);
        DEVELOPER_SIGNATURE_DEBUG = getLocalConfigInt(LOCAL_DEVELOPER_SIGNATURE_DEBUG);
        DEVELOPER_SIGNATURE_RELEASE = getLocalConfigInt(LOCAL_DEVELOPER_SIGNATURE_RELEASE);
        API_ENDPOINT = getLocalConfigString(LOCAL_API_ENDPOINT);
        API_PATH = getLocalConfigString(LOCAL_API_PATH);
        GCM_SENDER_ID = getLocalConfigString(LOCAL_GCM_SENDER_ID);
        GCM_SERVER_URL = getLocalConfigString(LOCAL_GCM_SERVER_URL);
        GCM_NEW_NOTIFICATION_INTENT = getLocalConfigString(LOCAL_GCM_NEW_NOTIFICATION_INTENT);
        API_KEYSTORE_TYPE = getLocalConfigString(LOCAL_API_KEYSTORE_TYPE);
        API_KEYSTORE_PASS = getLocalConfigString(LOCAL_API_KEYSTORE_PASS);
        API_USERNAME = getLocalConfigString(LOCAL_API_USERNAME);
        API_PASSWORD = getLocalConfigString(LOCAL_API_PASSWORD);
    }

    // /////////////////////////////////////////////////////////////
    //
    // Logs
    //
    // /////////////////////////////////////////////////////////////

    private static final int LOG_LEVEL_INFO = 3;
    private static final int LOG_LEVEL_WARNING = 2;
    private static final int LOG_LEVEL_ERROR = 1;
    private static final int LOG_LEVEL_NONE = 0;

    /**
     * Set this flag to LOG_LEVEL_NONE when releasing your application in order
     * to remove all logs.
     */
    private static final int LOG_LEVEL = COMPILATION_TARGET == COMPILATION_TARGET_DEBUG ? LOG_LEVEL_INFO
            : LOG_LEVEL_ERROR;

    /**
     * Indicates whether info logs are enabled. This should be true only when
     * developing/debugging an application/the library
     */
    public static final boolean INFO_LOGS_ENABLED = (LOG_LEVEL == LOG_LEVEL_INFO);

    /**
     * Indicates whether warning logs are enabled
     */
    public static final boolean WARNING_LOGS_ENABLED = INFO_LOGS_ENABLED
            || (LOG_LEVEL == LOG_LEVEL_WARNING);

    /**
     * Indicates whether error logs are enabled. Error logs are usually always
     * enabled, even in production releases.
     */
    public static final boolean ERROR_LOGS_ENABLED = WARNING_LOGS_ENABLED
            || (LOG_LEVEL == LOG_LEVEL_ERROR);

}
