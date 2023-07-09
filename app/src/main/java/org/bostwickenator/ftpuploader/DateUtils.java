package org.bostwickenator.ftpuploader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

class DateUtils {

    /**
     * Get the current date in our standard human readable filesystem safe format
     *
     * @return the date string
     */
    public static String getDate() {
        Calendar calendar = getDateTime().getCurrentTime(); 
        return new SimpleDateFormat("yyyy_MM_dd").format(calendar.getTime());
    }
}
