package org.csrdu.bayyina.helpers.extra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by nam on 12/3/14.
 */
public class DateTimeHelper {
    public static Date getCurrentDateTime(){
        Date cur_date = new Date();
        return cur_date;
    }

    public static Date parseStringToDate(final String date) {
        final Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setCalendar(cal);

        try {
            return format.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static int calculateDiffInMinutes(Date earlier_date, Date later_date) {
        if( earlier_date == null || later_date == null ) return 0;

        return (int)((later_date.getTime()/60000) - (earlier_date.getTime()/60000));
    }
}
