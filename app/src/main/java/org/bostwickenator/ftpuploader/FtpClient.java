package org.bostwickenator.ftpuploader;

import com.github.ma1co.pmcademo.app.Logger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class FtpClient {

    private final FTPClient ftpClient = new FTPClient();
    private final static String EMPTY_STRING = "";
    SettingsStore settingsStore = SettingsStore.getSettingsStore();

    public void connect() {

        try {
            ftpClient.connect(getStringWithEmptyAsDefault(SettingsActivity.SETTING_SERVER), Integer.parseInt(getStringWithEmptyAsDefault(SettingsActivity.SETTING_PORT)));
            ftpClient.login(getStringWithEmptyAsDefault(SettingsActivity.SETTING_USERNAME), getStringWithEmptyAsDefault(SettingsActivity.SETTING_PASSWORD));
            if (settingsStore.getBoolean(SettingsActivity.SETTING_PASSIVE, false)) {
                ftpClient.enterLocalPassiveMode();
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (Exception e) {
            Logger.error(e.toString());
        }
    }
    
    private String getStringWithEmptyAsDefault(String key) {
        return settingsStore.getString(key, EMPTY_STRING);
    }

    public void createAndUseAlbumForSubsequentOperations(String name) throws IOException {
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
