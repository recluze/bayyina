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


    private static final String BAYYINA_STANDARD_DATE_FORMAT = "zzz yyyy-MM-dd HH:mm:ss";


    public static String getStandardizedDateFormat(Date date) {
        final SimpleDateFormat format = new SimpleDateFormat(BAYYINA_STANDARD_DATE_FORMAT);
        return format.format(date);
    }

    public static Date getCurrentDateTime(){
        Date cur_date = new Date();
        return cur_date;
    }

    public static Date parseStringToDate(final String date) throws ParseException {
        final Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        final SimpleDateFormat format = new SimpleDateFormat(BAYYINA_STANDARD_DATE_FORMAT);
        format.setCalendar(cal);


        return format.parse(date);

    }

    public static int calculateDiffInMinutes(Date earlier_date, Date later_date) {
        if( earlier_date == null || later_date == null ) return 0;

        return (int)((later_date.getTime()/60000) - (earlier_date.getTime()/60000));
    }
}
