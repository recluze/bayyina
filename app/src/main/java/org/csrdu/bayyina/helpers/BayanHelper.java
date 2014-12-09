package org.csrdu.bayyina.helpers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Created by nam on 12/3/14.
 */
public class BayanHelper {

    private static final String TAG = "B_BayanHelper";

    public int updateBayanStatus(Context context, Uri uri, String newStatus) {
        Log.i(TAG, "Updating bayan status: for ["+ uri.toString() +"] to" + newStatus);

        ContentValues values = new ContentValues();
        values.put(BayanOpenHelper.BAYAN_STATUS, newStatus);

        int res = context.getContentResolver().update(uri, values, null, null);

        return res;
    }

    public int updateAllBayansStatus(Context context, Uri source_uri, String oldStatus, String newStatus) {
        Uri uri = BayanListProvider.CONTENT_URI;
        String [] projection = new String[] {
                BayanOpenHelper.BAYAN_ID,
                BayanOpenHelper.BAYAN_STATUS
        };

        String source_id = source_uri.getLastPathSegment();
        String selection = BayanOpenHelper.BAYAN_SOURCE_ID + "=" + source_id;

        if(oldStatus != null) {
            selection += " AND " + BayanOpenHelper.BAYAN_STATUS + "='" + oldStatus +"'";
        }

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, null, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                Uri update_uri = Uri.parse(BayanListProvider.CONTENT_URI.toString() + "/" + id);
                updateBayanStatus(context, update_uri, newStatus);
            } while(cursor.moveToNext());
        }

        return 0;
    }

    public boolean saveBayanData(Context context, String title, String url, String tags, String uploadedOn, int server_id, int source_id) {
        Log.i(TAG, String.format("Saving new bayan for source: [%s] Title[%s], url[%s], tags[%s], uploaded_on[%s]",
                source_id, title, url, tags, uploadedOn));

        ContentValues values = new ContentValues();
        values.put(BayanOpenHelper.BAYAN_TITLE, title);
        values.put(BayanOpenHelper.BAYAN_URL, url);
        values.put(BayanOpenHelper.BAYAN_TAGS, tags);
        values.put(BayanOpenHelper.BAYAN_UPLOADED_ON, uploadedOn);
        values.put(BayanOpenHelper.BAYAN_SOURCE_ID, source_id);
        values.put(BayanOpenHelper.BAYAN_SERVER_ID, server_id);
        values.put(BayanOpenHelper.BAYAN_STATUS, BayanOpenHelper.BAYAN_STATUS_NEW);

        int id = getBayanIdByServerId(context, source_id, server_id);
        if (id < 0) {
            Uri uri = context.getContentResolver().insert(BayanListProvider.CONTENT_URI, values);
        }



        return false;
    }

    private int getBayanIdByServerId(Context context, int source_id, int server_id) {
        int id = 0;

        ContentResolver cr = context.getContentResolver();
        // Submit the query and get a Cursor object back.
        String[] projection = new String[]{
                BayanOpenHelper.BAYAN_ID
        };
        Cursor cur = cr.query(Uri.parse(BayanListProvider.CONTENT_URI + "/" + BayanListProvider.BAYAN_SERVER_PATH + "/" + source_id + "/" + server_id),
                projection, null, null, null);

        if (cur.moveToFirst())
            id = cur.getInt(0);
        else
            id = -1;

        Log.i(TAG, "Found bayan ID: " + id);
        return id;
    }
}
