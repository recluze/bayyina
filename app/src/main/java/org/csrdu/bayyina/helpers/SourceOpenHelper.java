package org.csrdu.bayyina.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nam on 11/27/14.
 */
public class SourceOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    // public static final String SOURCE_DB_NAME = "sources.db";
    public static final String SOURCE_TABLE_NAME = "sources";

    public static final String SOURCE_ID = "_id";
    public static final String SOURCE_TITLE = "TITLE";
    public static final String SOURCE_URL = "URL";
    public static final String SOURCE_LAST_UPDATED = "LAST_UPDATED";

    public static final String SOURCE_STATUS = "STATUS";
    public static final String SOURCE_STATUS_SYNCED  = "SYNCED";
    public static final String SOURCE_STATUS_NEEDS_SYNCING = "NEEDS_SYNCING";

    public static final String SOURCE_TABLE_CREATE =
            "CREATE TABLE " + SOURCE_TABLE_NAME + " (" +
                    SOURCE_ID + " integer primary key autoincrement, " +
                    SOURCE_TITLE + " TEXT not null, " +
                    SOURCE_URL + " TEXT not null, " +
                    SOURCE_STATUS + " TEXT not null, " +
                    SOURCE_LAST_UPDATED + " TEXT);";

    private static final String TAG = "B_SourceOpenHelper";

    public SourceOpenHelper(Context context) {
        super(context, SOURCE_TABLE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "Source List DB constructor");
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SOURCE_TABLE_CREATE);
        Log.i(TAG, "Created Source List DB");


        String sql;
        sql = "insert into " + SOURCE_TABLE_NAME+ " ( " + SOURCE_TITLE+ "," + SOURCE_URL + "," + SOURCE_LAST_UPDATED+ "," + SOURCE_STATUS +" ) "
                + " values ( 'Tasawwuf', 'http://csrdu.org/bayyina/tasawwuf','2014-12-03 12:02:02', '"+SOURCE_STATUS_NEEDS_SYNCING+"' )";
        db.execSQL(sql);

        /*
        sql = "insert into " + SOURCE_TABLE_NAME+ " ( " + SOURCE_TITLE+ "," + SOURCE_URL + "," + SOURCE_LAST_UPDATED+ "," + SOURCE_STATUS +" ) "
                + " values ( 'Suluk', 'http://csrdu.org/bayyina/suluk','2014-12-03 13:05:01', '"+SOURCE_STATUS_SYNCED+"' )";
        db.execSQL(sql);

        sql = "insert into " + SOURCE_TABLE_NAME+ " ( " + SOURCE_TITLE+ "," + SOURCE_URL + "," + SOURCE_LAST_UPDATED+ "," + SOURCE_STATUS +" ) "
                + " values ( 'Sunnah Academy', 'http://sunnahacademy.org/bayyina','2014-12-03 01:05:02', '"+SOURCE_STATUS_SYNCED+"' )";
        db.execSQL(sql);
        */

        Log.i(TAG, "Inserted Source List DB dummy data");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgraded Source List DB");


    }
}
