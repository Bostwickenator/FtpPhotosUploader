package org.bostwickenator.ftpuploader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import com.github.ma1co.pmcademo.app.BaseActivity;
import com.github.ma1co.pmcademo.app.Logger;

public class MainActivity extends BaseActivity {
    private static final int NOT_INITIALIZED = -1;

    private ProgressBar progressBarUploadProgress;
    private TextView textViewPhotosToUploadCount, textViewUploadedCount, textViewUploadStatus;
    private View buttonUploadPhotos, buttonSettings;
    private View progressBarSpin;

    private final UploadRecordDatabase uploadRecordDatabase = UploadRecordDatabase.getInstance();

    private UploadTask mUploadTask;

    private int toUploadCount = NOT_INITIALIZED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        progressBarUploadProgress = (ProgressBar) findViewById(R.id.progressBarUploadProgress);

        buttonUploadPhotos = findViewById(R.id.buttonUploadPhotos);
        buttonSettings = findViewById(R.id.buttonSettings);

        progressBarSpin = findViewById(R.id.progressBarSpin);

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        buttonUploadPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUploadTask = new UploadTask();
                mUploadTask.execute();
            }
        });

        textViewPhotosToUploadCount = (TextView) findViewById(R.id.textViewPhotosToUploadCount);
        textViewUploadedCount = (TextView) findViewById(R.id.textViewUploadedCount);
        textViewUploadStatus = (TextView) findViewById(R.id.textViewUploadStatus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toUploadCount = NOT_INITIALIZED;
        updateNumberOfPhotos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mUploadTask != null) {
            mUploadTask.cancel(true);
        }
    }

    private void updateNumberOfPhotos() {

        if(toUploadCount == NOT_INITIALIZED) {
            List<File> files = getFilesToUpload();
            toUploadCount = files.size();
        }
        textViewPhotosToUploadCount.setText("" + toUploadCount);
        textViewUploadedCount.setText("" + uploadRecordDatabase.getUploadedCount());
        buttonUploadPhotos.setEnabled(toUploadCount != 0);
    }

    class UploadTask extends AsyncTask<Void, Integer, Void> {

        int totalFiles;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonUploadPhotos.setEnabled(false);
            buttonSettings.setEnabled(false);
            textViewUploadStatus.setText(R.string.connecting);
            progressBarSpin.setVisibility(View.VISIBLE);
            setAutoPowerOffMode(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            buttonUploadPhotos.setEnabled(true);
            buttonSettings.setEnabled(true);
            textViewUploadStatus.setText(R.string.statusComplete);
            progressBarSpin.setVisibility(View.GONE);
            setAutoPowerOffMode(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(progressBarUploadProgress.getMax() != totalFiles -1) {
                progressBarUploadProgress.setMax(totalFiles -1);
            }
            progressBarUploadProgress.setProgress(values[0]);
            toUploadCount--;
            updateNumberOfPhotos();
            Logger.info("Progress:" + values[0]);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                FtpClient ftp = new FtpClient();
                ftp.connect();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewUploadStatus.setText(R.string.statusUploading);
                    }
                });

                String albumName = "FTP_Uploader_" + DateUtils.getDate();

                ftp.createAndUseAlbumForSubsequentOperations(albumName);

                List<File> files = getFilesToUpload();

                totalFiles = files.size();
                for(int i = 0; i < totalFiles; i++) {
                    File file = files.get(i);

                    boolean result = ftp.storeFile(file);
                    if(result) {
                        uploadRecordDatabase.addFile(file);
                    }
                    publishProgress(i);
                    if(this.isCancelled()) {
                        break;
                    }
                }

                ftp.disconnect();
            } catch (Exception e) {
                Logger.error(e.toString());
            }
            return null;
        }
    }

    private List<File> getFilesToUpload(){
        List<File> files = FilesystemScanner.getImagesOnExternalStorage();

        if(SettingsStore.getSettingsStore().getBoolean(SettingsActivity.SETTING_UPLOAD_VIDEOS, false)) {
            List<File> videos = FilesystemScanner.getVideosOnExternalStorage();
            files.addAll(videos);
        }

        uploadRecordDatabase.filterFileList(files);

        return files;
    }
}