package org.bostwickenator.ftpuploader;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * Get the current date in our standard human readable filesystem safe format
     * @return the date string
     */
    public static String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyy_MM_dd");
        return format.format(new Date());
    }
}
