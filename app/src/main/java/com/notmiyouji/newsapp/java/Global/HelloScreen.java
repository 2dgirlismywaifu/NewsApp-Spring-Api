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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.RSSURL.HomePage;
import com.notmiyouji.newsapp.kotlin.SharedSettings.AppContextWrapper;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.WelcomeScreenShared;

public class HelloScreen extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    SharedPreferences prefs;
    TextView welcomeText;
    Button goHomePage;
    CheckBox showWelcomeScreen;
    protected void attachBaseContext(Context newBase) {
        //get language from shared preference
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(newBase);
        super.attachBaseContext(AppContextWrapper.wrap(newBase,loadFollowLanguageSystem.getLanguage()));
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_screen);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        if (new WelcomeScreenShared(this).getEnableWelcome()) {
            //go to main activity
            Intent intent = new Intent(HelloScreen.this, HomePage.class);
            ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
            finish();
        }
        else {
            welcomeText = findViewById(R.id.textView17);
            welcomeText.setText(getString(R.string.welcome_to) + getString(R.string.app_name));
            //create language shared preferences
//            LanguagePrefManager languagePrefManager = new LanguagePrefManager(this);
//            languagePrefManager.setLocal(Locale.getDefault().getDisplayLanguage());
            //create wallpaper header shared preferences
            SharedPreferences wallpaperHeader = getSharedPreferences("Wallpaper", MODE_PRIVATE);
            SharedPreferences.Editor wallpaperHeaderEditor = wallpaperHeader.edit();
            wallpaperHeaderEditor.putInt("path", R.drawable.anime_landscapes_background__11);
            wallpaperHeaderEditor.apply();
            showWelcomeScreen = findViewById(R.id.checkShowed);
            showWelcomeScreen.setOnCheckedChangeListener((buttonView, isChecked) -> {
                prefs = getSharedPreferences("welcomeScreen", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("showWelcomeScreen", isChecked);
                editor.apply();
            });
            //go home page
            goHomePage = findViewById(R.id.GoHomePage);
            goHomePage.setOnClickListener(v -> {
                Intent intent = new Intent(HelloScreen.this, HomePage.class);
                ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
                finish();
            });
        }

    }
}