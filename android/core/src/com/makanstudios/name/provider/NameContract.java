
package com.makanstudios.name.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class NameContract {

    public static final String CONTENT_AUTHORITY = "com.makanstudios.name.provider";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    interface SimpleTableColumns {

        String SIMPLE_COLUMN = "simple_column";
    }

    static final String PATH_SIMPLE_TABLE = "simple_table";

    public static class Sports implements SimpleTableColumns, BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SIMPLE_TABLE)
                .build();

        public static final String DEFAULT_SORT_ORDER = BaseColumns._ID + " ASC";
    }

    public static class UriHandler {

        public static Uri buildUri(Uri contentUri, long id) {
            return contentUri.buildUpon().appendPath("" + id).build();
        }

        public static Uri buildUri(Uri contentUri, String id) {
            return contentUri.buildUpon().appendPath(id).build();
        }

        public static String getId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
