package org.csrdu.bayyina.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.csrdu.bayyina.R;
import org.csrdu.bayyina.SourceList;
import org.csrdu.bayyina.helpers.DownloadHelper;
import org.csrdu.bayyina.helpers.SourceHelper;
import org.csrdu.bayyina.helpers.SourceOpenHelper;

import java.util.Timer;
import java.util.TimerTask;


public class SourceUpdateChecker extends Service {
    private static final String TAG = "B_SourceUpdateChecker";
    // TODO: make check interval read from settings
    private int check_interval_in_seconds = 60 * 10; // let's check every 10 mins for now

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Started service for Bayyina");

        checkRegularlyForBayanUpdates();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void checkRegularlyForBayanUpdates() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            Log.i(TAG, "Service executing in background checking for updates");
                            DownloaderTask performBackgroundTask = new DownloaderTask();
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            Log.i(TAG, "Service exception in running background task at interval");
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, check_interval_in_seconds * 1000); //execute in every 60s
    }

    private class DownloaderTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... x) {
            // don't want to check if have no network connectivity
            if (! DownloadHelper.haveNetworkConnection(getBaseContext())) {
                Log.i(TAG, "Don't have network connection. Not attempting to check for updates");
                return null;
            }

            Intent intent = new Intent(getBaseContext(), SourceList.class);
            PendingIntent pIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);

            SourceHelper sh = new SourceHelper();
            boolean are_some_bayans_refreshed = sh.updateAllSourceStatuses(getBaseContext());

            Log.i(TAG, "Service got are_some_bayans_refreshed: [" + are_some_bayans_refreshed + "]");

            if(are_some_bayans_refreshed) {
                Notification n = new Notification.Builder(getBaseContext())
                        .setContentTitle("Bayyina: New bayans are available")
                        .setContentText("Open app to update bayan list.")
                        .setSmallIcon(R.drawable.pen_drive)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true).getNotification();
                // .getNotification() above is deprecated. Should use .build() instead in API16+

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                notificationManager.notify(0, n);
                Log.i(TAG, "Service Notification sent.");
            }

            return null;
        }

    }

}
