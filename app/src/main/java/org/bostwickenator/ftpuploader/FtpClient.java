package org.bostwickenator.ftpuploader;

import com.github.ma1co.pmcademo.app.Logger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FtpClient {

    FTPClient ftpClient = new FTPClient();

    public void connect(){

        SettingsStore settingsStore = SettingsStore.getSettingsStore();
        try {
            ftpClient.connect(settingsStore.getString(SettingsActivity.SETTING_SERVER,""), Integer.parseInt(settingsStore.getString(SettingsActivity.SETTING_PORT, "")));
            ftpClient.login(settingsStore.getString(SettingsActivity.SETTING_USERNAME, ""), settingsStore.getString(SettingsActivity.SETTING_PASSWORD, ""));
            if(settingsStore.getBoolean(SettingsActivity.SETTING_PASSIVE, false)){
                ftpClient.enterLocalPassiveMode();
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (Exception e) {
            Logger.error(e.toString());
        }
    }


    public void createAlbum(String name) throws IOException {
        ftpClient.makeDirectory(name);
        ftpClient.changeWorkingDirectory(name);
    }

    public boolean storeFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        boolean result = ftpClient.storeFile(file.getName(), fis);
        fis.close();
        return result;
    }

    public void disconnect() throws IOException {
        ftpClient.disconnect();
    }
}
