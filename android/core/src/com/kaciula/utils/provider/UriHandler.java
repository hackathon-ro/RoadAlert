
package com.kaciula.utils.provider;

import android.net.Uri;

public class UriHandler {

    public static Uri buildUri(Uri contentUri, long id) {
        return contentUri.buildUpon().appendPath("" + id).build();
    }

    public static Uri buildUri(Uri contentUri, String id) {
        return contentUri.buildUpon().appendPath(id).build();
    }

    public static String getId(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    public static Uri addSyncAdapterParameter(Uri uri) {
        return uri.buildUpon().appendQueryParameter("sync", "true").build();
    }

    public static boolean hasSyncAdapterParameter(Uri uri) {
        return "true".equals(uri.getQueryParameter("sync"));
    }
}
