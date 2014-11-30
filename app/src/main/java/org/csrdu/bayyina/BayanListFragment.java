package org.csrdu.bayyina;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
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

import org.csrdu.bayyina.helpers.BayanListProvider;
import org.csrdu.bayyina.helpers.BayanOpenHelper;
import org.csrdu.bayyina.interfaces.GetSource;
import org.csrdu.bayyina.interfaces.SetSource;


public class BayanListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private static final String TAG = "B_BayanFragment";
    private GetSource listener;
    private ListView mListView;
    private SimpleCursorAdapter mAdapter;

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

        mListView = (ListView) rootView.findViewById(R.id.bayans_listview);
        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.bayan_lv_item_layout,
                null,
                new String[] {BayanOpenHelper.BAYAN_TITLE, BayanOpenHelper.BAYAN_URL},
                new int[] {R.id.bayan_title, R.id.bayan_uploaded_on},
                0 );

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        getActivity().getLoaderManager().initLoader(1, null, this);

        Log.d(TAG, "Loaded bayans fragment");

        return rootView;
    }



    /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SetSource) {
            listener = (GetSource) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement GetSource");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
    */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "Bayan List Loader onCreateLoader");

        Uri uri = BayanListProvider.CONTENT_URI;
        String [] projection = new String[] {
                BayanOpenHelper.BAYAN_ID,
                BayanOpenHelper.BAYAN_TITLE,
                BayanOpenHelper.BAYAN_TAGS,
                BayanOpenHelper.BAYAN_UPLOADED_ON,
                BayanOpenHelper.BAYAN_URL
        };
        return new CursorLoader(getActivity(), uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Bayan List Loader onLoadFinished");

        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}