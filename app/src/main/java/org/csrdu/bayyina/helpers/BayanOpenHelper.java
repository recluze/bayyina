package org.csrdu.bayyina.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nam on 11/27/14.
 */
public class BayanOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    // public static final String SOURCE_DB_NAME = "sources.db";
    public static final String BAYAN_TABLE_NAME = "bayans";

    public static final String BAYAN_ID = "_id";
    public static final String BAYAN_TITLE = "TITLE";
    public static final String BAYAN_URL = "URL";
    public static final String BAYAN_UPLOADED_ON = "UPLOADED_ON";
    public static final String BAYAN_TAGS = "TAGS";
    public static final String BAYAN_SOURCE_ID = "SOURCE_ID";
    public static final String BAYAN_SERVER_ID = "SERVER_ID";
    public static final String BAYAN_STATUS = "STATUS";
    public static final String BAYAN_STATUS_NEW = "NEW";
    public static final String BAYAN_STATUS_DOWNLOADED = "DOWNLOADED";

    public static final String BAYAN_TABLE_CREATE =
            "CREATE TABLE " + BAYAN_TABLE_NAME + " (" +
                    BAYAN_ID + " integer primary key autoincrement, " +
                    BAYAN_TITLE + " TEXT not null, " +
                    BAYAN_URL + " TEXT not null, " +
                    BAYAN_TAGS + " TEXT, " +
                    BAYAN_STATUS + " TEXT, " +
                    BAYAN_UPLOADED_ON + " TEXT, " +
                    BAYAN_SERVER_ID + " INTEGER, " +
                    BAYAN_SOURCE_ID + " INTEGER );";

    private static final String TAG = "B_BayanOpenHelper";

    public BayanOpenHelper(Context context) {
        super(context, BAYAN_TABLE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "Bayan List DB constructor");
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(BAYAN_TABLE_CREATE);
        Log.i(TAG, "Created Bayan List DB");


        String sql;
        sql = "insert into " + BAYAN_TABLE_NAME + " ( " + BAYAN_TITLE + "," + BAYAN_URL + "," + BAYAN_UPLOADED_ON + "," + BAYAN_SERVER_ID + "," + BAYAN_SOURCE_ID + "," + BAYAN_STATUS +") "
                + " values ( 'Tasawwuf Bayan 1', 'http://tasawwuf.org/b/1','2014-11-01', 1111, 1, 'NEW' )";
        db.execSQL(sql);

        sql = "insert into " + BAYAN_TABLE_NAME + " ( " + BAYAN_TITLE + "," + BAYAN_URL + "," + BAYAN_UPLOADED_ON + "," + BAYAN_SERVER_ID + "," + BAYAN_SOURCE_ID + "," + BAYAN_STATUS + ") "
                + " values ( 'Tasawwuf Bayan 2', 'http://tasawwuf.org/b/2','2014-11-02', 1112, 1, 'NEW' )";
        db.execSQL(sql);

        sql = "insert into " + BAYAN_TABLE_NAME + " ( " + BAYAN_TITLE + "," + BAYAN_URL + "," + BAYAN_UPLOADED_ON + "," + BAYAN_SERVER_ID + "," + BAYAN_SOURCE_ID + "," + BAYAN_STATUS + ") "
                + " values ( 'Suluk Bayan 1', 'http://suluk.org/b/2','2014-11-02', 1112, 2, 'NEW' )";
        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgraded Bayan List DB");
    }
}
