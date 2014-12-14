package org.csrdu.bayyina;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.csrdu.bayyina.helpers.DownloadHelper;
import org.csrdu.bayyina.helpers.SourceHelper;
import org.csrdu.bayyina.interfaces.GetSource;
import org.csrdu.bayyina.interfaces.SetSource;
import org.csrdu.bayyina.service.SourceUpdateChecker;


public class SourceList extends Activity {

    private static final String TAG = "B_SourceList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayan_list);
        // if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SourceListFragment())
                    .commit();
        // }

        SourceUpdaterTask task = new SourceUpdaterTask();
        // task.execute((Void) null);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // start the service to check for updates
        Intent intent = new Intent(getApplicationContext(), SourceUpdateChecker.class);
        startService(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_source_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.menu_source_add_new) {
            Intent i = new Intent(this, SourceMetadataActivity.class);
            startActivity(i);
        } else if (id == R.id.menu_source_update_all) {
            SourceUpdaterTask task = new SourceUpdaterTask();
            task.execute((Void) null);
        } else if (id == R.id.menu_source_about) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }



    private class SourceUpdaterTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;

        /*
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getBaseContext());
            pDialog.setMessage("Updating Sources ....");
            pDialog.show();
        }
        */

        @Override
        protected Void doInBackground(Void... x) {
            if (! DownloadHelper.haveNetworkConnection(getBaseContext())) {
                Log.i(TAG, "Don't have network connection. Not attempting to check for updates");
                return null;
            }
            
            SourceHelper sh = new SourceHelper();
            sh.updateAllSourceStatuses(getBaseContext());
            return null;
        }

        /*
        @Override
        protected void onPostExecute(Void nothing) {
            pDialog.dismiss();
        }
        */

    }
}
