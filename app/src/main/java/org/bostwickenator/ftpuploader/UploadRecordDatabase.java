package org.bostwickenator.ftpuploader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alex on 2016-05-28.
 */
public class UploadRecordDatabase {

    Set<File> files = new HashSet<>();

    private static UploadRecordDatabase theUploadRecordDatabase;
private static String FILE_NAME = "DB.TXT";

    private UploadRecordDatabase() {
        loadFileList();
    }

    public static UploadRecordDatabase getInstance() {
        if(theUploadRecordDatabase == null) {
            theUploadRecordDatabase = new UploadRecordDatabase();
        }
        return theUploadRecordDatabase;
    }


    public int getUploadedCount() {
        return files.size();
    }


    protected void loadFileList() {
        try {

            BufferedReader reader = new BufferedReader(new FileReader(FileGetter.getFile(FILE_NAME)));

            String fileName;
            while((fileName = reader.readLine()) != null){
                files.add(new File(fileName));
            }
            reader.close();

        } catch (IOException e) {}
    }

    public void filterFileList(List<File> filesToFilter){
        filesToFilter.removeAll(files);
    }

    protected void addFile(File file) {
        if(files.add(file)) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(FileGetter.getFile(FILE_NAME), true));
                writer.append(file.getAbsolutePath());
                writer.newLine();
                writer.close();
            } catch (IOException e) {
            }
        }
    }

    public void clearDatabase() {
        files.clear();
        FileGetter.getFile(FILE_NAME).delete();
    }
}
