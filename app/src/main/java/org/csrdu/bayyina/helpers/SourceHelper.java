package org.csrdu.bayyina.helpers;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.csrdu.bayyina.helpers.extra.DateTimeHelper;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.Locale;

public class SourceHelper {

    private static final String TAG = "B_SourceHelper";
    private int update_interval_in_minutes = 60;

    public boolean updateAllSourceStatuses(Context context) {
        Log.d(TAG, "Updating bayan status: for all sources");

        ContentResolver cr = context.getContentResolver();

        String[] projection = new String[]{
                SourceOpenHelper.SOURCE_ID,
                SourceOpenHelper.SOURCE_LAST_UPDATED,
                SourceOpenHelper.SOURCE_URL,
                SourceOpenHelper.SOURCE_TITLE
        };

        // we get all those which are synced because if it already needs syncing, we don't have to check it again
        Uri all_sources_uri = Uri.parse(SourceListProvider.CONTENT_URI.toString() + "/" + SourceListProvider.SOURCE_SYNCED_PATH);
        Cursor cur = cr.query(all_sources_uri, projection, null, null, null);

        if (cur.moveToFirst()) {
            do {
                int source_id = cur.getInt(cur.getColumnIndex(SourceOpenHelper.SOURCE_ID));
                String source_title = cur.getString(cur.getColumnIndex(SourceOpenHelper.SOURCE_TITLE));
                String source_url = cur.getString(cur.getColumnIndex(SourceOpenHelper.SOURCE_URL));
                String source_last_updated = cur.getString(cur.getColumnIndex(SourceOpenHelper.SOURCE_LAST_UPDATED));

                String source_uri = SourceListProvider.CONTENT_URI.toString() + "/" + source_id;
                // updateSourceStatus(context, source_id, source_title, source_url, source_last_updated, source_uri);

            } while (cur.moveToNext());
        }

        return true;
    }

    public boolean updateSourceStatus(Context context, int source_id, String title, String url, String source_last_updated, String source_uri) {

        Log.d(TAG, "Updating Source: " + source_id);
        DateFormat formatter;
        Date date;

        Date last_updated_date = DateTimeHelper.parseStringToDate(source_last_updated);
        Date cur_date = DateTimeHelper.getCurrentDateTime();

        int mins_diff = DateTimeHelper.calculateDiffInMinutes(last_updated_date, cur_date);
        Log.d(TAG, "Calculated diff in mins between [" + last_updated_date.toString() + "] and [" + cur_date.toString() + "] = [" + mins_diff + "]");

        if (mins_diff < update_interval_in_minutes)
            return false;

        try {
            String last_change_time = DownloadHelper.getSourceLastChangeTime(context, source_uri);
            Log.d(TAG, last_change_time);
        } catch (JSONException e) {
            // ignore as JSON is bad ... can't do anything about it.
            Log.d(TAG, "Bad JSON for source: " + source_uri + " at: " + url);
        }
        return true;
    }

    public boolean markSourceAsSynced(Context context, Uri uri) {
        Log.d(TAG, "Updating source status: for [" + uri.toString() + "] to" + SourceOpenHelper.SOURCE_STATUS_SYNCED);

        ContentValues values = new ContentValues();
        values.put(SourceOpenHelper.SOURCE_STATUS, SourceOpenHelper.SOURCE_STATUS_SYNCED);

        int res = context.getContentResolver().update(uri, values, null, null);

        return (res > 0);
    }
}
