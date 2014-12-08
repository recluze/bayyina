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
    private int update_interval_in_minutes = 1;

    public boolean updateAllSourceStatuses(Context context) {
        Log.i(TAG, "Updating bayan status: for all sources");

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

        boolean are_some_sources_refreshed = false;

        if (cur.moveToFirst()) {
            do {
                int source_id = cur.getInt(cur.getColumnIndex(SourceOpenHelper.SOURCE_ID));
                String source_title = cur.getString(cur.getColumnIndex(SourceOpenHelper.SOURCE_TITLE));
                String source_url = cur.getString(cur.getColumnIndex(SourceOpenHelper.SOURCE_URL));
                String source_last_updated = cur.getString(cur.getColumnIndex(SourceOpenHelper.SOURCE_LAST_UPDATED));

                String source_uri = SourceListProvider.CONTENT_URI.toString() + "/" + source_id;
                are_some_sources_refreshed = updateSourceStatus(context, source_id, source_title, source_url, source_last_updated, source_uri);

            } while (cur.moveToNext());
        }

        return are_some_sources_refreshed;
    }

    public boolean updateSourceStatus(Context context, int source_id, String title, String url, String source_last_updated, String source_uri) {
        Log.i(TAG, "Updating Source: " + source_id);
        DateFormat formatter;
        Date date;

        Date cur_date = DateTimeHelper.getCurrentDateTime();
        boolean force_update = false;

        Date last_updated_date = null;
        try {
            last_updated_date = DateTimeHelper.parseStringToDate(source_last_updated);
        } catch (ParseException e) {
            Log.i(TAG, "Bad record of last_update_time. Assuming now.");
            force_update = true;
            last_updated_date = cur_date;
        }

        if(!force_update) {
            int mins_diff = DateTimeHelper.calculateDiffInMinutes(last_updated_date, cur_date);
            Log.i(TAG, "Calculated diff in mins between [" + last_updated_date.toString() + "] and [" + cur_date.toString() + "] = [" + mins_diff + "]");

            if (mins_diff < update_interval_in_minutes) {
                Log.i(TAG, "Checked the source just a while ago. Ignoring refresh.");
                return false;
            }
        }

        String last_change_time_str = "";
        boolean is_updated = false;
        try {
            last_change_time_str = DownloadHelper.getSourceLastChangeTime(context, source_uri);
            Log.i(TAG, "Last change time: " + last_change_time_str);
            Date last_change_time = DateTimeHelper.parseStringToDate(last_change_time_str);
            int laggin_behind_source = DateTimeHelper.calculateDiffInMinutes(last_updated_date, last_change_time);

            Log.i(TAG, "Last updated locally on: " + last_updated_date.toString());
            Log.i(TAG, "Last remote change   on: " + last_change_time.toString());
            Log.i(TAG, "Mins diff : " + laggin_behind_source);

            SourceHelper sh = new SourceHelper();

            is_updated = false;
            if(force_update || laggin_behind_source > 0) {
                Log.i(TAG, "Source seems updated. We need to sync it. Updating local status");
                sh.markSourceAsNeedsSyncing(context, Uri.parse(source_uri));
                is_updated = true;
            } else {
                Log.i(TAG, "Source does not seem to have updated. Leaving as is.");
                is_updated = false;
            }

            // if everything went well, save the last_updated_date in source record
            sh.markSourceLastUpdatedOn(context, Uri.parse(source_uri), DateTimeHelper.getStandardizedDateFormat(last_change_time));

        } catch (JSONException e) {
            // ignore as JSON is bad ... can't do anything about it.
            Log.e(TAG, "Bad JSON for source: " + source_uri + " at: " + url + " -- " + e.getMessage());
            return false;
        } catch (ParseException e) {
            Log.e(TAG, "Invalid date for source: " + source_uri + " at: " + url + " -- " + last_change_time_str);
            return false;
        }
        return is_updated;
    }

    private boolean markSourceAsNeedsSyncing(Context context, Uri uri) {
        Log.i(TAG, "Updating source status: for [" + uri.toString() + "] to" + SourceOpenHelper.SOURCE_STATUS_NEEDS_SYNCING);

        ContentValues values = new ContentValues();
        values.put(SourceOpenHelper.SOURCE_STATUS, SourceOpenHelper.SOURCE_STATUS_NEEDS_SYNCING);

        int res = context.getContentResolver().update(uri, values, null, null);
        return (res > 0);
    }

    private boolean markSourceLastUpdatedOn(Context context, Uri uri, String last_updated_on) {
        Log.i(TAG, "Setting source last_updated_on to: " + last_updated_on);

        ContentValues values = new ContentValues();
        values.put(SourceOpenHelper.SOURCE_LAST_UPDATED, last_updated_on);

        int res = context.getContentResolver().update(uri, values, null, null);
        return (res > 0);
    }

    public boolean markSourceAsSynced(Context context, Uri uri) {
        Log.i(TAG, "Updating source status: for [" + uri.toString() + "] to" + SourceOpenHelper.SOURCE_STATUS_SYNCED);

        ContentValues values = new ContentValues();
        values.put(SourceOpenHelper.SOURCE_STATUS, SourceOpenHelper.SOURCE_STATUS_SYNCED);

        int res = context.getContentResolver().update(uri, values, null, null);

        return (res > 0);
    }

    public boolean deleteSourceById(Context context, long id) {
        String where = "_id = " + id;
        int res = context.getContentResolver().delete(SourceListProvider.CONTENT_URI, where, null);
        return (res > 0);
    }
}
