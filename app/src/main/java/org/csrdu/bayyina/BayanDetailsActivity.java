package org.csrdu.bayyina;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.csrdu.bayyina.helpers.BayanHelper;
import org.csrdu.bayyina.helpers.BayanListProvider;
import org.csrdu.bayyina.helpers.BayanOpenHelper;
import org.csrdu.bayyina.helpers.DownloadHelper;
import org.csrdu.bayyina.helpers.SourceListProvider;


public class BayanDetailsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String TAG = "B_BayanDetailsActivity";
    private Uri selected_source_uri;
    private TextView tv_bayan_title;
    private TextView tv_bayan_url;
    private TextView tv_bayan_uploaded_on;
    private TextView tv_bayan_tags;
    private TextView tv_bayan_status;
    private Button btn_bayan_details_download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Bundle bundle = getIntent().getExtras();
            selected_source_uri = Uri.parse(bundle.getString(SourceListProvider.CONTENT_ITEM_TYPE));
        } catch (NullPointerException e) {
            Toast.makeText(this, "No source given. Cannot load bayan list. ", Toast.LENGTH_SHORT);
        }

        Log.d(TAG, "Detailed activity shown for: " + selected_source_uri);

        getLoaderManager().initLoader(1, null, this);
        setContentView(R.layout.activity_bayan_details);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bayan_details, menu);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "Bayan List Loader onCreateLoader");

        String [] projection = new String[] {
                BayanOpenHelper.BAYAN_ID,
                BayanOpenHelper.BAYAN_TITLE,
                BayanOpenHelper.BAYAN_TAGS,
                BayanOpenHelper.BAYAN_UPLOADED_ON,
                BayanOpenHelper.BAYAN_URL,
                BayanOpenHelper.BAYAN_STATUS
        };

        String selected_source_id = this.selected_source_uri.getLastPathSegment();
        // String selection = BayanOpenHelper.BAYAN_SOURCE_ID + " = " + selected_source_id;

        Uri uri = Uri.parse(BayanListProvider.CONTENT_URI.toString() + "/" + selected_source_id);
        return new CursorLoader(this, uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        tv_bayan_title = (TextView) findViewById(R.id.tv_bayan_title);
        tv_bayan_url = (TextView) findViewById(R.id.tv_bayan_url);
        tv_bayan_uploaded_on = (TextView) findViewById(R.id.tv_bayan_uploaded_on);
        tv_bayan_tags = (TextView) findViewById(R.id.tv_bayan_tags);
        tv_bayan_status = (TextView) findViewById(R.id.tv_bayan_status);

        btn_bayan_details_download = (Button) findViewById(R.id.btn_bayan_details_download);
        btn_bayan_details_download.setOnClickListener(this);

        if (data.moveToFirst()) {
            tv_bayan_title.setText(data.getString(1));
            tv_bayan_tags.setText(data.getString(2));
            tv_bayan_url.setText(data.getString(4));
            tv_bayan_uploaded_on.setText(data.getString(3));
            tv_bayan_status.setText(data.getString(5));

            Log.d(TAG, "Loaded data");
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // do nothing
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_bayan_details_download:
                String url = tv_bayan_url.getText().toString();
                Log.d(TAG, "initiating download for: " + url);
                Toast.makeText(this,
                        "Initiating download: " + url, Toast.LENGTH_LONG)
                        .show();
                DownloadHelper.initiateDownload(this, url);

                // update the bayan record to set status as downloaded
                BayanHelper bh = new BayanHelper();
                bh.updateBayanStatus(this, selected_source_uri, BayanOpenHelper.BAYAN_STATUS_DOWNLOADED);
        }
    }
}
