package org.csrdu.bayyina;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;


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

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bayan_list, menu);
        return true;
    }

}
