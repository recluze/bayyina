package org.csrdu.bayyina.org.csrdu.bayyina.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nam on 11/27/14.
 */
public class SourceOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String SOURCE_TABLE_NAME = "dictionary";

    public static final String SOURCE_TITLE = "TITLE";
    public static final String SOURCE_URL = "URL";
    public static final String SOURCE_LAST_UPDATED = "LAST_UPDATED";

    public static final String SOURCE_TABLE_CREATE =
            "CREATE TABLE " + SOURCE_TABLE_NAME + " (" +
                    SOURCE_TITLE + "TEXT, " +
                    SOURCE_URL + " TEXT " +
                    SOURCE_LAST_UPDATED + "LAST_UPDATED DATE);";

    public SourceOpenHelper(Context context) {
        super(context, SOURCE_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SOURCE_TABLE_CREATE);
    }
}
