
package com.makanstudios.roadalert.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.kaciula.utils.provider.BasicContentProvider;
import com.kaciula.utils.provider.SelectionBuilder;

public class RoadAlertProvider extends BasicContentProvider {

    public RoadAlertProvider() {
        super(RoadAlertDatabase.class);
    }

    @Override
    protected UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        // FIXME: Build uri matcher
        // final String authority = YapperContract.CONTENT_AUTHORITY;

        // matcher.addURI(authority, YapperContract.PATH_SPORTS, SPORTS);

        return matcher;
    }

    @Override
    protected SelectionBuilder buildSimpleSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
        // FIXME: build selection based on match
        }

        return builder;
    }

    @Override
    protected SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
        // FIXME: build selection based on match
        }

        return builder;
    }

    @Override
    protected Uri doInsert(SQLiteDatabase db, Uri uri, int match, ContentValues values) {
        Uri newUri = null;
        switch (match) {
        // FIXME: Do inserts based on match
        /*
         * case X: db.insertOrThrow(Tables.SPORTS, null, values); newUri =
         * UriHandler.buildUri(Sports.CONTENT_URI,
         * values.getAsString(Sports.SPORT_ID)); break;
         */
        }

        return newUri;
    }

}
