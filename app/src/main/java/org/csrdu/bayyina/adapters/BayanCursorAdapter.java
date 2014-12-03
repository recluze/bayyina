package org.csrdu.bayyina.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.csrdu.bayyina.R;
import org.csrdu.bayyina.helpers.BayanOpenHelper;

/**
 * Created by nam on 12/3/14.
 */
public class BayanCursorAdapter extends CursorAdapter {

    public BayanCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.bayan_lv_item_layout, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ImageView iv_icon = (ImageView) view.findViewById(R.id.bayan_icon);
        final TextView tv_title = (TextView) view.findViewById(R.id.bayan_title);
        final TextView tv_uploaded_on = (TextView) view.findViewById(R.id.bayan_uploaded_on);
        final TextView tv_status = (TextView) view.findViewById(R.id.bayan_status);

        final String title = cursor.getString(cursor.getColumnIndex(BayanOpenHelper.BAYAN_TITLE));
        final String uploaded_on = cursor.getString(cursor.getColumnIndex(BayanOpenHelper.BAYAN_UPLOADED_ON));
        final String status = cursor.getString(cursor.getColumnIndex(BayanOpenHelper.BAYAN_STATUS));

        tv_title.setText(title);
        tv_uploaded_on.setText(uploaded_on);
        tv_status.setText(status);


        if(status.equals(BayanOpenHelper.BAYAN_STATUS_NEW)) {
            tv_title.setTypeface(null, Typeface.BOLD);
            iv_icon.setImageResource(R.drawable.sound_wave_up_fresh);
        } else {
            tv_title.setTypeface(null, Typeface.NORMAL);
            iv_icon.setImageResource(R.drawable.sound_wave_up);
        }

    }
}
