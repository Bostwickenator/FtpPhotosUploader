package org.bostwickenator.ftpuploader;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilesystemScanner {

    public FilesystemScanner() {

    }

    public static List<File> getImagesOnExternalStorage() {
        return getFilteredFileList(Environment.getExternalStorageDirectory(), ".jpg", ".arw");
    }

    public static List<File> getVideosOnExternalStorage() {
        return getFilteredFileList(Environment.getExternalStorageDirectory(), ".mts", ".mp4");
    }

    public static List<File> getFilteredFileList(File directory, String... extensions) {
        File[] subFiles = directory.listFiles();
        List<File> filtered = new ArrayList<>();
        if(subFiles != null){
            for (File f: subFiles) {
                String filename = f.getName().toLowerCase();
                if(f.isFile()){
                    for (String extension: extensions) {
                        if(filename.endsWith(extension)){
                            filtered.add(f);
                            break;
                        }
                    }
                } else if (f.isDirectory()){
                    filtered.addAll(getFilteredFileList(f, extensions));
                }
            }
        }
        return filtered;
    }
}
