package org.csrdu.bayyina;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.csrdu.bayyina.interfaces.GetSource;
import org.csrdu.bayyina.interfaces.SetSource;


public class BayanListFragment extends Fragment {
    private static final String TAG = "B_BayanFragment";
    private GetSource listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bayan_list, container, false);
        Uri selected_uri = listener.getSelectedSource();

        TextView t = (TextView) rootView.findViewById(R.id.hello_bayan);
        t.setText(selected_uri.toString());

        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SetSource) {
            listener = (GetSource) activity;
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
