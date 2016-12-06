package org.bostwickenator.ftpuploader;

import android.os.Environment;

import com.github.ma1co.pmcademo.app.Logger;

import java.io.File;
import java.util.regex.Pattern;

public class FileGetter {

    private static Pattern pattern = Pattern.compile("\\S{1,8}\\.\\S{1,3}");

    /**
     * Get a file handle in our storage directory
     *
     * @param name the name of the file
     * @return the file handle
     */
    public static File getFile(String name) {
        if(!checkIfValidFilename(name)){
            Logger.error("Files must match the 8.3 filename format");
            return null;
        }
        File ret = new File(Environment.getExternalStorageDirectory(), "PRIVATE/FTP/" + name);
        ret.getParentFile().mkdirs();
        return ret;
    }

    /**
     * It turns out the camera is very fussy about file name formats presumibly because of the constrains of FAT memory cards.
     * @param name
     * @return if the name is valid or not
     */
    static boolean checkIfValidFilename(String name) {
        return pattern.matcher(name).matches();
    }
}
