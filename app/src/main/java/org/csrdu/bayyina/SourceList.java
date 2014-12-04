package org.csrdu.bayyina;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.csrdu.bayyina.helpers.SourceHelper;
import org.csrdu.bayyina.interfaces.GetSource;
import org.csrdu.bayyina.interfaces.SetSource;


public class SourceList extends Activity {

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
        task.execute((Void) null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bayan_list, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private class SourceUpdaterTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... x) {
            SourceHelper sh = new SourceHelper();
            sh.updateAllSourceStatuses(getBaseContext());
            return null;
        }

    }
}
