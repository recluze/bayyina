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
import org.csrdu.bayyina.helpers.extra.DateTimeHelper;

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
        final TextView tv_tags = (TextView) view.findViewById(R.id.bayan_tags);

        final String title = cursor.getString(cursor.getColumnIndex(BayanOpenHelper.BAYAN_TITLE));
        final String uploaded_on = DateTimeHelper.getShortDateFormat(
                            cursor.getString(cursor.getColumnIndex(BayanOpenHelper.BAYAN_UPLOADED_ON)),
                            "yyyy-MM-dd");

        final String status = cursor.getString(cursor.getColumnIndex(BayanOpenHelper.BAYAN_STATUS));
        final String tags = cursor.getString(cursor.getColumnIndex(BayanOpenHelper.BAYAN_TAGS));

        tv_title.setText(title);
        tv_uploaded_on.setText(uploaded_on);
        tv_tags.setText(tags);


        if(status.equals(BayanOpenHelper.BAYAN_STATUS_NEW)) {
            // tv_title.setTypeface(null, Typeface.BOLD);
            tv_title.setTextColor(context.getResources().getColor(android.R.color.black));
            tv_uploaded_on.setTextColor(context.getResources().getColor(android.R.color.black));
            iv_icon.setImageResource(R.drawable.star_green);
        } else {
            tv_title.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            tv_uploaded_on.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            iv_icon.setImageResource(R.drawable.star_grey);
        }

    }
}
