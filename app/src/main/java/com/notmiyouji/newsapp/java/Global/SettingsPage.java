package com.notmiyouji.newsapp.java.Global;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Signed.SettingsLogined;
import com.notmiyouji.newsapp.java.Signed.SignInForm;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadThemeShared;
import com.notmiyouji.newsapp.kotlin.SharedSettings.UseChromeShared;
import com.notmiyouji.newsapp.kotlin.SharedSettings.WelcomeScreenShared;

public class SettingsPage extends AppCompatActivity {

    RelativeLayout aboutApp, signIn, selLanguage, selTheme;
    Intent intent;
    DrawerLayout drawerLayout;
    SwitchMaterial useChrome, showWelcome;
    SharedPreferences prefs;
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    LoadThemeShared loadThemeShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //check shared preferences for language
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Set Background form SharedPreference
        drawerLayout = findViewById(R.id.settings_banner_guest);
        if (loadBackground() != drawerLayout.getBackground().getCurrent().getConstantState().getChangingConfigurations()) {
            drawerLayout.setBackground(ResourcesCompat.getDrawable(getResources(), loadBackground(), null));
        }
        //About Application
        aboutApp = findViewById(R.id.about_application);
        aboutApp.setOnClickListener(v -> {
            //go to about application
            intent = new Intent(SettingsPage.this, AboutApplication.class);
            startActivity(intent);
        });
        //Sign In
        signIn = findViewById(R.id.signin_menu);
        signIn.setOnClickListener(v -> {
            //go to sign in page
            intent = new Intent(SettingsPage.this, SignInForm.class);
            startActivity(intent);
        });
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Change Wallpaper
        Button changeWallpaper = findViewById(R.id.changeWallpaper);
        changeWallpaper.setOnClickListener(v -> {
            //go to change wallpaper
            intent = new Intent(SettingsPage.this, WallpaperHeader.class);
            startActivity(intent);
        });
        //Selected Langauge
        selLanguage = findViewById(R.id.selected_language);
        selLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SettingsPage.this, ChangeLanguage.class);
                startActivity(intent);
            }
        });
        //Selected Theme
        selTheme = findViewById(R.id.change_theme);
        selTheme.setOnClickListener(v -> {
            //go to change theme
            intent = new Intent(SettingsPage.this, ChangeTheme.class);
            startActivity(intent);
        });
        //Switch WebView to Chrome Custom Tabs
        useChrome = findViewById(R.id.switchChrome);
        useChrome.setChecked(new UseChromeShared(this).getEnableChrome());
        useChrome.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs = getSharedPreferences("useChrome", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("useChromeDefault", isChecked);
            editor.apply();
        });
        //Switch Show Welcome
        showWelcome = findViewById(R.id.turnoffWelcome);
        showWelcome.setChecked(new WelcomeScreenShared(this).getEnableWelcome());
        showWelcome.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs = getSharedPreferences("welcomeScreen", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("showWelcomeScreen", isChecked);
            editor.apply();
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }

    private int loadBackground() {
        prefs = getSharedPreferences("Wallpaper", MODE_PRIVATE);
        return prefs.getInt("path", drawerLayout.getBackground().getCurrent().getConstantState().getChangingConfigurations());
    }

    public void onResume() {
        super.onResume();
        if (loadBackground() != drawerLayout.getBackground().getCurrent().getConstantState().getChangingConfigurations()) {
            drawerLayout.setBackground(ResourcesCompat.getDrawable(getResources(), loadBackground(), null));
        }
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}