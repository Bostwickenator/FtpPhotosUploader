package org.bostwickenator.ftpuploader;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyy_MM_dd");
        return format.format(new Date());
    }
}
