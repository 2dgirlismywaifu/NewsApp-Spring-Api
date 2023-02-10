package com.notmiyouji.newsapp.java.Global;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.RSSURL.HomePage;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.WelcomeScreenShared;

public class HelloScreen extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    SharedPreferences prefs;
    TextView welcomeText;
    Button goHomePage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
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
            //go home page
            goHomePage = findViewById(R.id.GoHomePage);
            goHomePage.setOnClickListener(v -> {
                prefs = getSharedPreferences("welcomeScreen", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("showWelcomeScreen", true);
                editor.apply();
                Intent intent = new Intent(HelloScreen.this, HomePage.class);
                ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
                finish();
            });
        }

    }
}