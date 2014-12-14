package org.csrdu.bayyina;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        WebView wvAbout = (WebView) findViewById(R.id.wv_about);
        String about_data = getString(R.string.about_content);

        about_data = "<small>" +
                "Bayyina is an app developed under by Sunnah Academy under the guidance of Shaykh Mustafa " +
                "Kamal <i>(damat barkatahum)</i>. <br /><br />" +
                "The objective of the app is to allow users to get updates about Bayans from the sites " +
                "operated by Shaykh Mustafa (DB) and his beloved <i>murshid</i> Shaykh Zulfiqar Ahmad Naqshbandi" +
                "<i>(damat barkatuhum)</i>.  Details about both can be seen on http://tasawwuf.org and http://suluk.com <br /><br /> " +
                "In order to get updates from any source, it must be added to the main screen. Afterwards," +
                "whenever a new bayan is available on the source, you will get a notification. " +
                "To download a bayan, simply click on its name and then click 'Download'. <br /> <br />" +
                "If you have any queries or suggestions for improvement, please contact the developer on bayyina@csrdu.org" +
                "</small>";
        wvAbout.loadData(about_data, "text/html", "utf-8");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
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
}
