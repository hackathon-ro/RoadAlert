
package com.makanstudios.roadalert.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.kaciula.utils.misc.LogUtils;
import com.makanstudios.roadalert.provider.RoadAlertContract.AlertColumns;

public class RoadAlertDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "roadalert.db";

    private static final int VERSION_LAUNCH = 4;

    private static final int DATABASE_VERSION = VERSION_LAUNCH;

    public interface Tables {

        String ALERTS = "alerts";
    }

    public RoadAlertDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.ALERTS + " (" + BaseColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + AlertColumns.ALERT_ID + " INTEGER NOT NULL DEFAULT 0,"
                + AlertColumns.LAT + " INTEGER NOT NULL DEFAULT 0,"
                + AlertColumns.LON + " INTEGER NOT NULL DEFAULT 0,"
                + AlertColumns.TIMESTAMP + " INTEGER NOT NULL DEFAULT 0,"
                + AlertColumns.NOTIFIED + " BOOLEAN,"
                + AlertColumns.DEVICE_ID + " STRING,"
                + "UNIQUE (" + AlertColumns.ALERT_ID + ") ON CONFLICT REPLACE"
                + ")");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly())
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.d("onUpgrade() from " + oldVersion + " to " + newVersion);

        int version = oldVersion;
        /*
         * switch (version) { case VERSION_LAUNCH: // Enter the changes to the
         * tables here version = VERSION_WORK_ORDERS; }
         */

        LogUtils.d("after upgrade logic, at version " + version);
        if (version != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.ALERTS);
            onCreate(db);
        }
    }
}
