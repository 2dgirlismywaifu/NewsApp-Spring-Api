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

package com.notmiyouji.newsapp.java.general;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.sharedsettings.AppContextWrapper;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadThemeShared;

public class AboutApplication extends AppCompatActivity {

    ShapeableImageView imageView;
    RelativeLayout githubbtn, twitterbtn;
    Intent intent;
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    LoadThemeShared loadThemeShared;

    protected void attachBaseContext(Context newBase) {
        //get language from shared preference
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(newBase);
        super.attachBaseContext(AppContextWrapper.wrap(newBase, loadFollowLanguageSystem.getLanguage()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        imageView = findViewById(R.id.owner_avatar);
        //load image github owner
        String path = "https://avatars.githubusercontent.com/u/59259855?s=400&u=e458277f4cca06aeb82adcd83641b8c92008947c&v=4"; //my avater github
        LoadImageURL loadImageURL = new LoadImageURL(path);
        loadImageURL.loadImageOwner(imageView);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
        //Go to GitHub Profile
        githubbtn = findViewById(R.id.linearGithub);
        githubbtn.setOnClickListener(v -> {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/2dgirlismywaifu"));
            startActivity(intent);
        });
        //Go to Twitter Profile
        twitterbtn = findViewById(R.id.linearTwitter);
        twitterbtn.setOnClickListener(v -> {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MyWaifuis2DGirl"));
            startActivity(intent);
        });
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            ActivityOptions.makeSceneTransitionAnimation(AboutApplication.this).toBundle();
            finish();
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }

    public void onResume() {
        super.onResume();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}