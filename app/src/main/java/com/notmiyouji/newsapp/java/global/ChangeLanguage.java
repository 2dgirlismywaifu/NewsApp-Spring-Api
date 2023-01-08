package com.notmiyouji.newsapp.java.global;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.global.recycleviewadapter.LanguageAdpater;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;

public class ChangeLanguage extends AppCompatActivity {

    RecyclerView recyclerView;
    LanguagePrefManager languagePrefManager;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        languagePrefManager = new LanguagePrefManager(getBaseContext());
        languagePrefManager.setLocal(languagePrefManager.getLang());
        languagePrefManager.loadLocal();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        recyclerView = findViewById(R.id.lang_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChangeLanguage.this, LinearLayoutManager.VERTICAL, false);
        LanguageAdpater languageAdpater = new LanguageAdpater(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(languageAdpater);
        languageAdpater.notifyDataSetChanged();
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }
}