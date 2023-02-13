/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notmiyouji.newsapp.java.Global;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.RecycleViewAdapter.LanguageAdpater;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadThemeShared;

public class ChangeLanguage extends AppCompatActivity {

    RecyclerView recyclerView;
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    LoadThemeShared loadThemeShared;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
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