package org.bostwickenator.ftpuploader;

import android.os.Environment;

import java.io.File;

public class FileGetter {

    /**
     * Get a file handle in our storage directory
     * @param name the name of the file
     * @return the file handle
     */
    public static File getFile(String name){
        File ret = new File(Environment.getExternalStorageDirectory(), "PRIVATE/FTP/" + name);
        ret.getParentFile().mkdirs();
        return ret;
    }
}
