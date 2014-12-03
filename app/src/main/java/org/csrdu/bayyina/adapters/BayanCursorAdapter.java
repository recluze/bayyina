package org.csrdu.bayyina.adapters;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

/**
 * Created by nam on 12/3/14.
 */
public class BayanCursorAdapter extends SimpleCursorAdapter {
    public BayanCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }
}
