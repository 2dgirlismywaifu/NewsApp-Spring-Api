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

package com.notmiyouji.newsapp.java.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.util.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.recycleview.WallpaperHeaderAdpater;
import com.notmiyouji.newsapp.kotlin.sharedsettings.AppContextWrapper;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;

import java.util.Objects;

public class WallpaperHeader extends AppCompatActivity {
    private WallpaperHeaderAdpater wallpaperHeaderAdpater;
    private RecyclerView recyclerView;

    protected void attachBaseContext(Context newBase) {
        //get language from shared preference
        LoadFollowLanguageSystem loadFollowLanguageSystem = new LoadFollowLanguageSystem(newBase);
        super.attachBaseContext(AppContextWrapper.wrap(newBase, Objects.requireNonNull(loadFollowLanguageSystem.getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_header);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        recyclerView = findViewById(R.id.wallpaperRecyclerView);
        //Put Picture to RecycleView
        //Need use thread to load 20 pictures
        LoadWallpaperList(this);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
    }

    private void LoadWallpaperList(AppCompatActivity activity) {
        Thread loadWallpaer = new Thread(() -> {
            wallpaperHeaderAdpater = new WallpaperHeaderAdpater(activity);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            runOnUiThread(() -> recyclerView.setAdapter(wallpaperHeaderAdpater));
        });
        loadWallpaer.start();
    }
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            ActivityOptions.makeSceneTransitionAnimation(WallpaperHeader.this).toBundle();
            finish();
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }

    public void onResume() {
        super.onResume();
    }
}