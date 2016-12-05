package org.bostwickenator.ftpuploader;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.github.ma1co.pmcademo.app.BaseActivity;

public class SettingsActivity extends BaseActivity {

    public static final String SETTING_UPLOAD_VIDEOS = "upload_videos";
    public static final String SETTING_DELETE_AFTER_UPLOAD = "delete_after_upload";

    public static final String SETTING_USERNAME = "username";
    public static final String SETTING_PASSWORD = "password";
    public static final String SETTING_SERVER = "server";
    public static final String SETTING_PORT = "port";
    public static final String SETTING_PASSIVE = "passive";

    private SettingsStore mSharedPreferences;
    //EditText username, password, server, port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = SettingsStore.getSettingsStore();
        setContentView(R.layout.activity_settings);


        View buttonClearDatabase = findViewById(R.id.buttonClearUploadDatabase);

        buttonClearDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadRecordDatabase.getInstance().clearDatabase();
            }
        });

        setupCheckbox(R.id.checkBoxDeleteAfterUpload, SETTING_DELETE_AFTER_UPLOAD);
        setupCheckbox(R.id.checkBoxUploadVideos, SETTING_UPLOAD_VIDEOS);
        setupCheckbox(R.id.checkBoxPassive, SETTING_PASSIVE);

        setupEditText(R.id.editTextUsername, SETTING_USERNAME);
        setupEditText(R.id.editTextPassword, SETTING_PASSWORD);
        setupEditText(R.id.editTextServer, SETTING_SERVER);
        setupEditText(R.id.editTextPort, SETTING_PORT);
    }

    private void setupCheckbox(int id, final String setting) {
        CheckBox checkBox = (CheckBox) findViewById(id);
        checkBox.setChecked(mSharedPreferences.getBoolean(setting, false));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSharedPreferences.putBoolean(setting, isChecked);
            }
        });
    }

    private void setupEditText(int id, String setting) {
        EditText editText = (EditText) findViewById(id);
        editText.setText(mSharedPreferences.getString(setting, ""));
        editText.addTextChangedListener(getTextWatcher(setting));
    }

    private TextWatcher getTextWatcher(final String setting) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSharedPreferences.putString(setting, s.toString());
            }
        };
    }


}
