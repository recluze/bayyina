package org.csrdu.bayyina.adapters;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.csrdu.bayyina.R;
import org.csrdu.bayyina.helpers.SourceOpenHelper;


public class SourceCursorAdapter extends CursorAdapter {

    public SourceCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.source_lv_item_layout, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ImageView iv_icon = (ImageView) view.findViewById(R.id.icon);
        final TextView tv_title = (TextView) view.findViewById(R.id.source_title);
        final TextView tv_last_updated = (TextView) view.findViewById(R.id.source_last_updated);

        final String title = cursor.getString(cursor.getColumnIndex(SourceOpenHelper.SOURCE_TITLE));
        final String last_updated = cursor.getString(cursor.getColumnIndex(SourceOpenHelper.SOURCE_LAST_UPDATED));
        final String status = cursor.getString(cursor.getColumnIndex(SourceOpenHelper.SOURCE_STATUS));

        tv_title.setText(title);
        tv_last_updated.setText(last_updated);

        if(status.equals(SourceOpenHelper.SOURCE_STATUS_NEEDS_SYNCING)) {
            tv_title.setTypeface(null, Typeface.BOLD);
            iv_icon.setImageResource(R.drawable.book_plain_fresh);
        } else {
            tv_title.setTypeface(null, Typeface.NORMAL);
            iv_icon.setImageResource(R.drawable.black_book_plain);
        }
    }
}
