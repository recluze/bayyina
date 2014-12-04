package org.csrdu.bayyina.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

/**
 * Created by nam on 12/3/14.
 */
public class SourceUpdateChecker extends Service {
    private static final String TAG = "B_SourceUpdateChecker";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class DownloaderTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... x) {
            // TBD

            return null;
        }

    }

}
