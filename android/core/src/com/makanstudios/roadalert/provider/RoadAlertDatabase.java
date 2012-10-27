
package com.makanstudios.roadalert.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.kaciula.utils.misc.LogUtils;

public class RoadAlertDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "name.db";

    private static final int VERSION_LAUNCH = 1;

    private static final int DATABASE_VERSION = VERSION_LAUNCH;

    public interface Tables {

        // FIXME: Create a real table
        String SIMPLE_TABLE = "simple_table";
    }

    public RoadAlertDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.SIMPLE_TABLE + " (" + BaseColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT" + ")");
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
            db.execSQL("DROP TABLE IF EXISTS " + Tables.SIMPLE_TABLE);
            onCreate(db);
        }
    }
}
