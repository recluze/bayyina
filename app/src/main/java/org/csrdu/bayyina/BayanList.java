package org.csrdu.bayyina;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.csrdu.bayyina.helpers.BayanHelper;
import org.csrdu.bayyina.helpers.BayanOpenHelper;
import org.csrdu.bayyina.helpers.SourceListProvider;


public class BayanList extends Activity {
    private Uri selected_source_uri;
    BayanListFragment blf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayan_list);

        blf = new BayanListFragment();

        // if (savedInstanceState == null) {
        getFragmentManager().beginTransaction()
                .add(R.id.container, blf)
                .commit();
        // }

        try {
            Bundle bundle = getIntent().getExtras();
            selected_source_uri = Uri.parse(bundle.getString(SourceListProvider.CONTENT_ITEM_TYPE));
        } catch (NullPointerException e) {
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

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
        if (id == R.id.menu_bayan_mark_all_read) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            // Actually mark all NEW bayans for this source as MARKED_DOWNLOADED
                            BayanHelper bh = new BayanHelper();
                            bh.updateAllBayansStatus(getBaseContext(), selected_source_uri,
                                    BayanOpenHelper.BAYAN_STATUS_NEW,
                                    BayanOpenHelper.BAYAN_STATUS_MARKED_DOWNLOADED);
                            blf.onResume();

                            Toast.makeText(getBaseContext(), "Marked all bayans as downloaded.", Toast.LENGTH_SHORT).show();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to mark all bayans for this source as downloaded?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
