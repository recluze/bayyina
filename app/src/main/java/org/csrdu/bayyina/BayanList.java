package org.csrdu.bayyina;

import android.app.Activity;
import android.os.Bundle;


public class BayanList extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayan_list);
        // if (savedInstanceState == null) {
        getFragmentManager().beginTransaction()
                .add(R.id.container, new BayanListFragment())
                .commit();
        // }

    }


}
