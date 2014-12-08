package org.csrdu.bayyina;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.csrdu.bayyina.adapters.SourceCursorAdapter;
import org.csrdu.bayyina.helpers.DownloadHelper;
import org.csrdu.bayyina.helpers.SourceHelper;
import org.csrdu.bayyina.helpers.SourceListProvider;
import org.csrdu.bayyina.helpers.SourceOpenHelper;
import org.csrdu.bayyina.interfaces.SetSource;

/**
* Created by nam on 11/30/14.
*/
public class SourceListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final String TAG = "B_SourcesFragment";
    private ListView mListView;
    private CursorAdapter mAdapter;
    private SetSource listener;
    private ProgressDialog pDialog;

    protected LoaderManager.LoaderCallbacks<Cursor> loaderCallBack;

    public SourceListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_source_list, container, false);
        // return rootView;

        mListView = (ListView) rootView.findViewById(R.id.sources_listview);

        mAdapter = new SourceCursorAdapter(getActivity(),
                null,
                0 );

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnCreateContextMenuListener(this);
        registerForContextMenu(mListView);

        loaderCallBack = this;

        Log.i(TAG, "Loaded sources fragment");

        DownloadSourceDetails task = new DownloadSourceDetails();
        task.execute((Void) null);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Source List Loader onCreateLoader");

        Uri uri = SourceListProvider.CONTENT_URI;
        String [] projection = new String[] {
                SourceOpenHelper.SOURCE_ID,
                SourceOpenHelper.SOURCE_TITLE,
                SourceOpenHelper.SOURCE_URL,
                SourceOpenHelper.SOURCE_LAST_UPDATED,
                SourceOpenHelper.SOURCE_STATUS
        };
        return new CursorLoader(getActivity(), uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "Source List Loader onLoadFinished");

        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // super.onItemClick(parent, view, position, id);
        Intent i = new Intent(getActivity(), BayanList.class);
        String todoUri = SourceListProvider.CONTENT_URI + "/" + id;
        i.putExtra(SourceListProvider.CONTENT_ITEM_TYPE, todoUri);
        startActivity(i);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.i(TAG, "onCreateContextMenu called for source list view.");
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_souce_list_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            Log.e("", "bad menuInfo", e);
            return false;
        }


        switch (item.getItemId()) {
            case R.id.menu_source_context_delete_source:
                Log.i(TAG, "Delete source requested");

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Log.i(TAG, "Requesting deletion of source: " + info.id);
                                SourceHelper sh = new SourceHelper();
                                sh.deleteSourceById(getActivity(), info.id);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete this source?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /* Class for handling updation of bayans from a particular source
     * Helpful tutorial here: http://www.vogella.com/tutorials/AndroidBackgroundProcessing/article.html
     */

    private class DownloadSourceDetails extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Refreshing Source Information ....");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void ... urls) {
            if (! DownloadHelper.haveNetworkConnection(getActivity())) {
                Log.i(TAG, "Don't have network connection. Not attempting to check for updates");
                return null;
            }

            Boolean response = true;

            try {
                SourceHelper sh = new SourceHelper();
                response = sh.updateAllSourceStatuses(getActivity());
            } catch (Exception e) {
                response = false;
            }

            return response;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            getActivity().getLoaderManager().initLoader(0, null, loaderCallBack);
            pDialog.dismiss();
        }
    }
}
