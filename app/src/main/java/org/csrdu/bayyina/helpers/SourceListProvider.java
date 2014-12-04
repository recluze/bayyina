package org.csrdu.bayyina.helpers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


public class SourceListProvider extends ContentProvider {
    private static final String TAG = "B_SourceListProvider";
    private SourceOpenHelper mDB;

    private static final String AUTHORITY = "org.csrdu.bayyina.sources.SourceListProvider";
    public static final int SOURCES = 100;
    public static final int SOURCE_ID = 110;
    public static final int SOURCE_SYNCED_ID = 120;

    public static final String SOURCES_BASE_PATH = "sources";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SOURCES_BASE_PATH);

    public static final String SOURCE_SYNCED_PATH = "is_synced";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                                                    + "/bayyina-source";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                                                    + "/bayyina-source";

    @Override
    public boolean onCreate() {
        Log.i(TAG, "Created SourceListContentProvider");

        mDB = new SourceOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SourceOpenHelper.SOURCE_TABLE_NAME);

        Log.i(TAG, "Query received.");

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case SOURCE_ID:
                Log.i(TAG, "Got request for source");
                queryBuilder.appendWhere(mDB.SOURCE_ID + "="
                    + uri.getLastPathSegment());
                break;
            case SOURCES:
                // no filter
                Log.i(TAG, "Got request for all sources");
                break;
            case SOURCE_SYNCED_ID:
                Log.i(TAG, "Got request for source needs sync ");
                queryBuilder.appendWhere(mDB.SOURCE_STATUS + "= '"
                        + SourceOpenHelper.SOURCE_STATUS_SYNCED +"'");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();

        int rowsUpdated =  0;
        switch (uriType) {
            case SOURCE_ID:
                String id = uri.getLastPathSegment();
                rowsUpdated = sqlDB.update(SourceOpenHelper.SOURCE_TABLE_NAME,
                        values,
                        SourceOpenHelper.SOURCE_ID + "=" + id,
                        null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }



    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);



    static {
        sURIMatcher.addURI(AUTHORITY, SOURCES_BASE_PATH, SOURCES);
        sURIMatcher.addURI(AUTHORITY, SOURCES_BASE_PATH + "/#", SOURCE_ID);
        sURIMatcher.addURI(AUTHORITY, SOURCES_BASE_PATH + "/" + SOURCE_SYNCED_PATH, SOURCE_SYNCED_ID);
    }
}
