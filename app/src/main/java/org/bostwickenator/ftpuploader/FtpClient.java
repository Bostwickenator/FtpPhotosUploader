package org.bostwickenator.ftpuploader;

import com.github.ma1co.pmcademo.app.Logger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static java.lang.Integer.parseInt;
import static org.bostwickenator.ftpuploader.SettingsActivity.SETTING_PASSIVE;
import static org.bostwickenator.ftpuploader.SettingsActivity.SETTING_PASSWORD;
import static org.bostwickenator.ftpuploader.SettingsActivity.SETTING_PORT;
import static org.bostwickenator.ftpuploader.SettingsActivity.SETTING_SERVER;
import static org.bostwickenator.ftpuploader.SettingsActivity.SETTING_TARGET_DIR;
import static org.bostwickenator.ftpuploader.SettingsActivity.SETTING_USERNAME;
import static org.bostwickenator.ftpuploader.SettingsStore.settingsStore;

class FtpClient {

    private final FTPClient ftpClient = new FTPClient();
    private final static String EMPTY_STRING = "";
    private SettingsStore settingsStore = settingsStore();

    public void connect() {

        try {
            this.ftpClient.connect(getStringWithEmptyAsDefault(SETTING_SERVER), parseInt(this.settingsStore.getString(SETTING_PORT, "21")));
            this.ftpClient.login(getStringWithEmptyAsDefault(SETTING_USERNAME), getStringWithEmptyAsDefault(SETTING_PASSWORD));
            if (this.settingsStore.getBoolean(SETTING_PASSIVE, false)) {
                this.ftpClient.enterLocalPassiveMode();
            }
            this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            if (!getStringWithEmptyAsDefault(SETTING_TARGET_DIR).equals(EMPTY_STRING)) {
                if (!this.ftpClient.changeWorkingDirectory(getStringWithEmptyAsDefault(SETTING_TARGET_DIR))) {
                    Logger.error("failed changing to target dir");
                }
            }
        } catch (Exception e) {
            Logger.error(e.toString());
        }
    }
    
    private String getStringWithEmptyAsDefault(String key) {
        return this.settingsStore.getString(key, EMPTY_STRING);
    }

    public void createAndUseAlbumForSubsequentOperations(String name) throws IOException {
        this.ftpClient.makeDirectory(name);
        this.ftpClient.changeWorkingDirectory(name);
    }

    public boolean storeFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        boolean result = this.ftpClient.storeFile(file.getName(), fis);
        fis.close();
        return result;
    }

    public void disconnect() throws IOException {
        this.ftpClient.disconnect();
    }
}
