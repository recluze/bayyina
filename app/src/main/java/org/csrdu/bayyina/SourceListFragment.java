package org.csrdu.bayyina;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.csrdu.bayyina.helpers.SourceListProvider;
import org.csrdu.bayyina.helpers.SourceOpenHelper;
import org.csrdu.bayyina.interfaces.SetSource;

/**
* Created by nam on 11/30/14.
*/
public class SourceListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final String TAG = "B_SourcesFragment";
    private ListView mListView;
    private SimpleCursorAdapter mAdapter;
    private SetSource listener;

    public SourceListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_source_list, container, false);
        // return rootView;

        mListView = (ListView) rootView.findViewById(R.id.listview);
        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.source_lv_item_layout,
                null,
                new String[] {SourceOpenHelper.SOURCE_TITLE, SourceOpenHelper.SOURCE_URL, SourceOpenHelper.SOURCE_LAST_UPDATED},
                new int[] {R.id.source_title, R.id.source_last_updated},
                0 );

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        getActivity().getLoaderManager().initLoader(0, null, this);

        Log.d(TAG, "Loaded sources fragment");


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "Source List Loader onCreateLoader");

        Uri uri = SourceListProvider.CONTENT_URI;
        String [] projection = new String[] {
                SourceOpenHelper.SOURCE_ID,
                SourceOpenHelper.SOURCE_TITLE,
                SourceOpenHelper.SOURCE_URL,
                SourceOpenHelper.SOURCE_LAST_UPDATED
        };
        return new CursorLoader(getActivity(), uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Source List Loader onLoadFinished");

        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
        Toast.makeText(getActivity(),
                "Click ListItem Number " + position, Toast.LENGTH_LONG)
                .show();
        */

        // super.onItemClick(parent, view, position, id);
        // Intent i = new Intent(this, TodoDetailActivity.class);
//        Intent i = new Intent();
        Uri todoUri = Uri.parse(SourceListProvider.CONTENT_URI + "/" + id);
//        i.putExtra(SourceListProvider.CONTENT_ITEM_TYPE, todoUri);
//
//        startActivity(i);

        listener.OnSourceChanged(todoUri);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new BayanListFragment())
                .commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SetSource) {
            listener = (SetSource) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement SetSource");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
