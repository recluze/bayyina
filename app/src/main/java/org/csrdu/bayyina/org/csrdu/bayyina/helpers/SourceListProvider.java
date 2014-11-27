package org.csrdu.bayyina.org.csrdu.bayyina.helpers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;


public class SourceListProvider extends ContentProvider {
    private SourceOpenHelper mDB;

    private static final String AUTHORITY = "org.csrdu.bayyina.sources.SourceListProvider";
    public static final int SOURCES = 100;
    public static final int SOURCE_ID = 110;

    private static final String SOURCES_BASE_PATH = "sources";
    public static final Uri CONTENT_URI = Uri.parse("content" + AUTHORITY + "/" + SOURCES_BASE_PATH);

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                                                    + "/bayyina-source";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                                                    + "/bayyina-source";

    @Override
    public boolean onCreate() {
        mDB = new SourceOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SourceOpenHelper.SOURCE_TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case SOURCE_ID:
                queryBuilder.appendWhere(SOURCE_ID + "="
                    + uri.getLastPathSegment());
                break;
            case SOURCES:
                // no filter
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
        sURIMatcher.addURI(AUTHORITY, SOURCES_BASE_PATH, SOURCES);
        sURIMatcher.addURI(AUTHORITY, SOURCES_BASE_PATH + "/#", SOURCE_ID);
    }
}
