package org.csrdu.bayyina.helpers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


public class BayanListProvider extends ContentProvider {
    private static final String TAG = "B_BayanListProvider";
    private BayanOpenHelper mDB;

    private static final String AUTHORITY = "org.csrdu.bayyina.bayans.BayanListProvider";
    public static final int BAYANS = 100;
    public static final int BAYAN_ID = 110;
    public static final int SOURCE_ID = 120; // need to filter by source as well

    private static final String BAYANS_BASE_PATH = "bayans";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BAYANS_BASE_PATH);

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                                                    + "/bayyina-bayan";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                                                    + "/bayyina-bayan";

    @Override
    public boolean onCreate() {
        Log.d(TAG, "Created BayanListContentProvider");

        mDB = new BayanOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(BayanOpenHelper.BAYAN_TABLE_NAME);

        Log.d(TAG, "Query received.");

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case BAYAN_ID:
                Log.d(TAG, "Got request for bayan");
                queryBuilder.appendWhere(mDB.BAYAN_ID + "="
                    + uri.getLastPathSegment());
                break;
            case BAYANS:
                // no filter
                Log.d(TAG, "Got request for all bayans");
                break;
            case SOURCE_ID:
                Log.d(TAG, "Got request for source");
                queryBuilder.appendWhere(mDB.BAYAN_SOURCE_ID + "="
                        + uri.getLastPathSegment());
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
        return 0;
    }


    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BAYANS_BASE_PATH, BAYANS);
        sURIMatcher.addURI(AUTHORITY, BAYANS_BASE_PATH + "/#", BAYAN_ID);
        sURIMatcher.addURI(AUTHORITY, BAYANS_BASE_PATH + "/#", SOURCE_ID);
    }
}
