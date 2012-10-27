
package com.makanstudios.roadalert.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.kaciula.utils.provider.BasicContentProvider;
import com.kaciula.utils.provider.SelectionBuilder;
import com.kaciula.utils.provider.UriHandler;
import com.makanstudios.roadalert.provider.RoadAlertContract.Alerts;
import com.makanstudios.roadalert.provider.RoadAlertDatabase.Tables;

public class RoadAlertProvider extends BasicContentProvider {

    private static final int ALERTS = 100;

    private static final int ALERTS_ID = 101;

    public RoadAlertProvider() {
        super(RoadAlertDatabase.class);
    }

    @Override
    protected UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RoadAlertContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, RoadAlertContract.PATH_ALERTS, ALERTS);
        matcher.addURI(authority, RoadAlertContract.PATH_ALERTS + "/*", ALERTS_ID);

        return matcher;
    }

    @Override
    protected SelectionBuilder buildSimpleSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
            case ALERTS:
                return builder.table(Tables.ALERTS);
            case ALERTS_ID:
                return builder.table(Tables.ALERTS).where(Alerts.ALERT_ID + "= ?",
                        UriHandler.getId(uri));
        }

        return builder;
    }

    @Override
    protected SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
            case ALERTS:
                return builder.table(Tables.ALERTS);
            case ALERTS_ID:
                return builder.table(Tables.ALERTS).where(Alerts.ALERT_ID + "= ?",
                        UriHandler.getId(uri));
        }

        return builder;
    }

    @Override
    protected Uri doInsert(SQLiteDatabase db, Uri uri, int match, ContentValues values) {
        Uri newUri = null;
        switch (match) {
            case ALERTS:
                db.insertOrThrow(Tables.ALERTS, null, values);
                newUri =
                        UriHandler.buildUri(Alerts.CONTENT_URI,
                                values.getAsString(Alerts.ALERT_ID));
                break;
        }

        return newUri;
    }

}
