
package com.kaciula.utils.provider;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.kaciula.utils.misc.LogUtils;

public abstract class BasicContentProvider extends ContentProvider {

    private static final String TAG = LogUtils.makeLogTag(BasicContentProvider.class
            .getSimpleName());

    private Class<SQLiteOpenHelper> mHelperClass;

    private SQLiteOpenHelper mHelper;

    private final UriMatcher mUriMatcher;

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public BasicContentProvider(Class helperClass) {
        mHelperClass = helperClass;
        mUriMatcher = buildUriMatcher();
    }

    @Override
    public boolean onCreate() {
        try {
            mHelper = mHelperClass.getConstructor(Context.class).newInstance(getContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        LogUtils.d(
                TAG,
                "delete(uri=" + uri + ", selection=" + selection + ", args="
                        + Arrays.toString(selectionArgs) + ")");

        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        boolean syncToNetwork = UriHandler.hasSyncAdapterParameter(uri);
        final SelectionBuilder builder = buildSimpleSelection(uri, match);
        int no = builder.where(selection, selectionArgs).delete(db);
        getContext().getContentResolver().notifyChange(uri, null, syncToNetwork);
        return no;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        LogUtils.d(TAG, "insert(uri=" + uri + ", values=" + values.toString() + ")");

        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        boolean syncToNetwork = UriHandler.hasSyncAdapterParameter(uri);

        Uri newUri = doInsert(db, uri, match, values);
        getContext().getContentResolver().notifyChange(newUri, null, syncToNetwork);
        return newUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        LogUtils.d(TAG, "query(uri=" + uri + ", proj=" + Arrays.toString(projection)
                + ", selection=" + selection + ", args=" + Arrays.toString(selectionArgs) + ")");

        final SQLiteDatabase db = mHelper.getReadableDatabase();

        final int match = mUriMatcher.match(uri);
        switch (match) {
            default: {
                // Most cases are handled with simple SelectionBuilder
                final SelectionBuilder builder = buildExpandedSelection(uri, match);
                Cursor cursor = builder.where(selection, selectionArgs).query(db, projection,
                        sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        LogUtils.d(TAG, "update(uri=" + uri + ", values=" + values.toString() + ")");

        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        boolean syncToNetwork = UriHandler.hasSyncAdapterParameter(uri);
        final SelectionBuilder builder = buildSimpleSelection(uri, match);
        int no = builder.where(selection, selectionArgs).update(db, values);
        getContext().getContentResolver().notifyChange(uri, null, syncToNetwork);
        return no;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Build and return a {@link UriMatcher} that catches all {@link Uri}
     * variations supported by this {@link ContentProvider}.
     */
    protected abstract UriMatcher buildUriMatcher();

    protected abstract SelectionBuilder buildSimpleSelection(Uri uri, int match);

    protected abstract SelectionBuilder buildExpandedSelection(Uri uri, int match);

    protected abstract Uri doInsert(SQLiteDatabase db, Uri uri, int match, ContentValues values);
}
