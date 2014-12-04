package org.csrdu.bayyina;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.csrdu.bayyina.adapters.BayanCursorAdapter;
import org.csrdu.bayyina.helpers.BayanListProvider;
import org.csrdu.bayyina.helpers.BayanOpenHelper;
import org.csrdu.bayyina.helpers.DownloadHelper;
import org.csrdu.bayyina.helpers.SourceListProvider;
import org.csrdu.bayyina.interfaces.GetSource;
import org.csrdu.bayyina.interfaces.SetSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class BayanListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private static final String TAG = "B_BayanFragment";
    private GetSource listener;
    private ListView mListView;
    private BayanCursorAdapter mAdapter;
    private Uri selected_source_uri;
    private ProgressDialog pDialog;

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bayan_list, container, false);
        // Uri selected_uri = listener.getSelectedSource();

        try {
            Bundle bundle = getActivity().getIntent().getExtras();
            selected_source_uri = Uri.parse(bundle.getString(SourceListProvider.CONTENT_ITEM_TYPE));
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "No source given. Cannot load bayan list. ", Toast.LENGTH_SHORT);
            return rootView;
        }

        mListView = (ListView) rootView.findViewById(R.id.bayans_listview);
        /*
        mAdapter = new BayanCursorAdapter(getActivity(),
                R.layout.bayan_lv_item_layout,
                null,
                new String[] {BayanOpenHelper.BAYAN_TITLE, BayanOpenHelper.BAYAN_UPLOADED_ON, BayanOpenHelper.BAYAN_STATUS},
                new int[] {R.id.bayan_title, R.id.bayan_uploaded_on, R.id.bayan_status},
                0 );
        */
        mAdapter = new BayanCursorAdapter(getActivity(),
                null,
                0 );

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        Log.i(TAG, "Loaded bayans fragment");

        loaderCallBack = this;

        DownloadBayansFromSource task = new DownloadBayansFromSource();
        task.execute(new String[] { selected_source_uri.toString() });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getLoaderManager().restartLoader(1, null, loaderCallBack);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String [] projection = new String[] {
                BayanOpenHelper.BAYAN_ID,
                BayanOpenHelper.BAYAN_TITLE,
                BayanOpenHelper.BAYAN_TAGS,
                BayanOpenHelper.BAYAN_UPLOADED_ON,
                BayanOpenHelper.BAYAN_URL,
                BayanOpenHelper.BAYAN_STATUS
        };

        Intent i = new Intent(getActivity(), BayanDetailsActivity.class);
        String todoUri = BayanListProvider.CONTENT_URI + "/" + id;
        i.putExtra(SourceListProvider.CONTENT_ITEM_TYPE, todoUri);
        startActivity(i);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Bayan List Loader onCreateLoader");


        String [] projection = new String[] {
                BayanOpenHelper.BAYAN_ID,
                BayanOpenHelper.BAYAN_TITLE,
                BayanOpenHelper.BAYAN_TAGS,
                BayanOpenHelper.BAYAN_UPLOADED_ON,
                BayanOpenHelper.BAYAN_URL,
                BayanOpenHelper.BAYAN_STATUS
        };

        String selected_source_id = this.selected_source_uri.getLastPathSegment();

        Uri uri = Uri.parse(BayanListProvider.CONTENT_URI.toString() + "/" + BayanListProvider.SOURCE_PATH + "/" + selected_source_id);
        return new CursorLoader(getActivity(), uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "Bayan List Loader onLoadFinished");

        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    /* Class for handling updation of bayans from a particular source
     * Helpful tutorial here: http://www.vogella.com/tutorials/AndroidBackgroundProcessing/article.html
     */

    private class DownloadBayansFromSource extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Updating Bayan List ....");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String ... urls) {
            // TODO: check for network connectivity
            Boolean response = true;
            for (String url : urls) {
                DownloadHelper.updateBayanList(getActivity(), url);
            }
            return response;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            getActivity().getLoaderManager().initLoader(1, null, loaderCallBack);
            pDialog.dismiss();
        }
    }
}