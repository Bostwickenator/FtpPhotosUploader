package org.bostwickenator.ftpuploader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DateUtils {

    /**
     * Get the current date in our standard human readable filesystem safe format
     * @return the date string
     */
    public static String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyy_MM_dd", Locale.US);
        return format.format(new Date());
    }
}
