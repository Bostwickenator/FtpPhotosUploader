package org.bostwickenator.ftpuploader;

import android.os.Environment;

import java.io.File;

public class FileGetter {

    public static File getFile(String name){
        File ret = new File(Environment.getExternalStorageDirectory(), "PRIVATE/FTP/" + name);
        ret.getParentFile().mkdirs();
        return ret;
    }
}
