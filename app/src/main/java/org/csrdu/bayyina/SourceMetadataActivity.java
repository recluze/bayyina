package org.csrdu.bayyina;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.csrdu.bayyina.helpers.SourceHelper;
import org.csrdu.bayyina.helpers.SourceListProvider;
import org.csrdu.bayyina.helpers.SourceOpenHelper;


public class SourceMetadataActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "B_SourceMetadataActivity";
    private String selected_source_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_metadata);

        try {
            Bundle bundle = getIntent().getExtras();
            selected_source_id = bundle.getString(SourceListProvider.CONTENT_ITEM_TYPE);
            getLoaderManager().initLoader(1, null, this);

            // also need to load the exiting details.
        } catch (NullPointerException e) {
            selected_source_id = null; // we're adding a new one
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_source_metadata, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.metadata_action_save) {
            Long source_id = null;
            if(this.selected_source_id != null) {
                source_id = Long.parseLong(this.selected_source_id);
            }

            TextView tv_title = (TextView) findViewById(R.id.source_metadata_title);
            TextView tv_url = (TextView) findViewById(R.id.source_metadata_url);

            String title = tv_title.getText().toString().trim();
            String url = tv_url.getText().toString().trim();

            if ("".equals(title) || "".equals(url)) {
                Toast.makeText(this, "Please provide a title and URL for the source.", Toast.LENGTH_SHORT).show();
                return true;
            }

            SourceHelper sh = new SourceHelper();
            sh.saveSource(this, source_id, title, url);

            Toast.makeText(this, "Source saved.", Toast.LENGTH_SHORT).show();
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Bayan List Loader onCreateLoader");

        String [] projection = new String[] {
                SourceOpenHelper.SOURCE_ID,
                SourceOpenHelper.SOURCE_TITLE,
                SourceOpenHelper.SOURCE_URL,
        };

        Uri uri;
        if(selected_source_id != null) {
            uri = Uri.parse(SourceListProvider.CONTENT_URI.toString() + "/" + selected_source_id);
        } else {
            uri = SourceListProvider.CONTENT_URI;
        }
        return new CursorLoader(this, uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(selected_source_id != null) {
            TextView tv_source_title = (TextView) findViewById(R.id.source_metadata_title);
            TextView tv_source_url = (TextView) findViewById(R.id.source_metadata_url);

            if (data.moveToFirst()) {
                tv_source_title.setText(data.getString(1));
                tv_source_url.setText(data.getString(2));

                Log.i(TAG, "Loaded data");
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
