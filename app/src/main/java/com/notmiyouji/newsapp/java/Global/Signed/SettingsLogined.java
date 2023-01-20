package com.notmiyouji.newsapp.java.Global.Signed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.SharedSettings.LanguagePrefManager;

public class SettingsLogined extends AppCompatActivity {
    LanguagePrefManager languagePrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        languagePrefManager = new LanguagePrefManager(getBaseContext());
        languagePrefManager.setLocal(languagePrefManager.getLang());
        languagePrefManager.loadLocal();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_logined);
    }

    public void onResume() {
        super.onResume();
        languagePrefManager.setLocal(languagePrefManager.getLang());
        languagePrefManager.loadLocal();
    }
}