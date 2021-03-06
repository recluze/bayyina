package org.csrdu.bayyina.helpers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.csrdu.bayyina.BayanList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nam on 12/1/14.
 */
public class DownloadHelper {
    private static final String TAG = "B_DownloadHelper";

    public static void initiateDownload(Context context, String uri) {
        final DownloadManager dm = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(uri));

        String filename = Uri.parse(uri).getLastPathSegment();
        request.allowScanningByMediaScanner();
        request.setDescription("Bayyina Download");
        request.setTitle(filename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // use external storage if available
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // File sdcard = Environment.getExternalStorageDirectory();
            // File dir = new File(sdcard.getAbsolutePath() + "/bayyina/");
            // dir.mkdir();
            request.setDestinationInExternalPublicDir("bayyina/", filename);
        }
        request.setVisibleInDownloadsUi(true);

        final long enqueue = dm.enqueue(request);


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {
                            Toast.makeText(context,
                                    "Download complete", Toast.LENGTH_LONG)
                                    .show();

                        }
                    }
                } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
                    Toast.makeText(context,
                            "Notification clicked", Toast.LENGTH_LONG)
                            .show();
                }

                context.unregisterReceiver(this);
            }


        };

        // context.registerReceiver(receiver, new IntentFilter(
        //        DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public static void updateBayanList(Context context, String source_uri) {
        Log.i(TAG, "Checking source for new bayans: " + source_uri);

        int source_id = Integer.parseInt(Uri.parse(source_uri).getLastPathSegment());

        String source_url = getSourceUrlFromUri(context, source_uri);
        String get_bayan_list_url = source_url + "/" + BayyinaApi.GET_BAYAN_LIST;

        Log.i(TAG, "Updating bayan list from source: " + get_bayan_list_url);

        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(get_bayan_list_url);
        String response = "";
        try {
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Log.i(TAG, "Received response: \n" + response);
        processBayanList(context, response, source_id);

        SourceHelper sh = new SourceHelper();
        sh.markSourceAsSynced(context, Uri.parse(source_uri));
    }

    private static boolean processBayanList(Context context, String response, int source_id) {
        boolean res = true;
        try {
            JSONArray json = new JSONArray(response);

            Log.i(TAG, "Found total bayans: " + json.length());

            for (int i = 0; i < json.length(); i++) {
                JSONObject json_data = json.getJSONObject(i);
                String title = json_data.getString("title");
                String url = json_data.getString("url");
                String tags = json_data.getString("tags");
                String uploadedOn = json_data.getString("uploaded_on");
                int server_id = json_data.getInt("server_id");

                BayanHelper bh = new BayanHelper();
                res = bh.saveBayanData(context, title, url, tags, uploadedOn, server_id, source_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return res;
    }



    public static String getSourceUrlFromUri(Context context, String uri) {
        ContentResolver cr = context.getContentResolver();
        // Submit the query and get a Cursor object back.
        String[] projection = new String[]{
                SourceOpenHelper.SOURCE_URL
        };
        Cursor cur = cr.query(Uri.parse(uri), projection, null, null, null);

        String url = null;
        if (cur.moveToFirst()) {
            url = cur.getString(0);
        }
        return url.replaceAll("/$", "");
    }

    public static String getSourceLastChangeTime(Context context, String source_uri) throws JSONException {
        Log.i(TAG, "Getting source last change time: " + source_uri);

        int source_id = Integer.parseInt(Uri.parse(source_uri).getLastPathSegment());

        String source_url = getSourceUrlFromUri(context, source_uri);
        String get_source_details_url = source_url + "/" + BayyinaApi.GET_SOURCE_DETAILS;

        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(get_source_details_url);
        String response = "";
        try {
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Received response: \n" + response);

        JSONObject json = new JSONObject(response);

        String last_updated = json.getString("last_updated_on");
        return last_updated;

    }


    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
