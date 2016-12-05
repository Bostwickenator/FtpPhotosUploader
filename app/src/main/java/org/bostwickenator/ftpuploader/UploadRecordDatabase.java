package org.bostwickenator.ftpuploader;

import com.github.ma1co.pmcademo.app.Logger;

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
 * A simple store to record file lists in. Deals with persistence internally.
 */
public class UploadRecordDatabase {

    private final Set<File> files = new HashSet<>();

    private static UploadRecordDatabase theUploadRecordDatabase;
    private static final String FILE_NAME = "DB.TXT";

    private UploadRecordDatabase() {
        loadFileList();
    }

    public static UploadRecordDatabase getInstance() {
        if(theUploadRecordDatabase == null) {
            theUploadRecordDatabase = new UploadRecordDatabase();
        }
        return theUploadRecordDatabase;
    }


    /**
     * Get the number of records in the database
     * @return the count of items
     */
    public int getUploadedCount() {
        return files.size();
    }


    private void loadFileList() {
        try {

            BufferedReader reader = new BufferedReader(new FileReader(FileGetter.getFile(FILE_NAME)));

            String fileName;
            while((fileName = reader.readLine()) != null){
                files.add(new File(fileName));
            }
            reader.close();

        } catch (IOException e) {
            Logger.error("Could not loadFileList");
            Logger.error(e.toString());
        }
    }

    public void filterFileList(List<File> filesToFilter){
        filesToFilter.removeAll(files);
    }

    public void addFile(File file) {
        if(files.add(file)) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(FileGetter.getFile(FILE_NAME), true));
                writer.append(file.getAbsolutePath());
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                Logger.error("Could not addFile to upload record database");
                Logger.error(e.toString());
            }
        }
    }

    public void clearDatabase() {
        files.clear();
        if(!FileGetter.getFile(FILE_NAME).delete()) {
            Logger.error("Could not delete database");
        }
    }
}
