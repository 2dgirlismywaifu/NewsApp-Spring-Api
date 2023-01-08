package com.notmiyouji.newsapp.java.global;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;

public class VerifyAccountForm extends AppCompatActivity {
    LanguagePrefManager languagePrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        languagePrefManager = new LanguagePrefManager(getBaseContext());
        languagePrefManager.setLocal(languagePrefManager.getLang());
        languagePrefManager.loadLocal();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_account_form);
    }

    public void onResume() {
        super.onResume();
        languagePrefManager.setLocal(languagePrefManager.getLang());
        languagePrefManager.loadLocal();
    }
}