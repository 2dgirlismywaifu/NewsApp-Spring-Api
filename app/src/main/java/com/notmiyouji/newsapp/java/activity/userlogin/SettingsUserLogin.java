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

package com.notmiyouji.newsapp.java.activity.userlogin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.activity.AboutApplication;
import com.notmiyouji.newsapp.java.activity.ChangeLanguage;
import com.notmiyouji.newsapp.java.activity.ChangeTheme;
import com.notmiyouji.newsapp.java.activity.WallpaperHeader;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadThemeShared;
import com.notmiyouji.newsapp.kotlin.sharedsettings.SaveUserLogined;
import com.notmiyouji.newsapp.kotlin.sharedsettings.UseChromeShared;
import com.notmiyouji.newsapp.kotlin.sharedsettings.WelcomeScreenShared;

import java.util.Objects;

public class SettingsUserLogin extends AppCompatActivity {
    private LoadFollowLanguageSystem loadFollowLanguageSystem;
    private LoadThemeShared loadThemeShared;
    private TextView fullName, username;
    private DrawerLayout drawerLayout;
    private Intent intent;
    private SharedPreferences prefs;
    private GetUserLogin getUserLogin;

    @SuppressLint({"SetTextI18n", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_logined);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Set textView
        fullName = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        getUserLogin = new GetUserLogin(this);
        fullName.setText(getUserLogin.getFullName());
        username.setText("@" + getUserLogin.getUsername());
        //Set Background form SharedPreference
        drawerLayout = findViewById(R.id.settings_banner_logined);
        if (loadBackground() != drawerLayout.getBackground().getCurrent().getConstantState().getChangingConfigurations()) {
            drawerLayout.setBackground(ResourcesCompat.getDrawable(getResources(), loadBackground(), null));
        }
        //About Application
        RelativeLayout aboutApp = findViewById(R.id.about_application);
        aboutApp.setOnClickListener(v -> {
            //go to about application
            intent = new Intent(SettingsUserLogin.this, AboutApplication.class);
            startActivity(intent);
        });
        //Load avatar
        ShapeableImageView avatar = findViewById(R.id.avatar_user_logined);
        LoadImageURL loadImageURL = new LoadImageURL(getUserLogin.getAvatar());
        loadImageURL.loadImageUser(avatar);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Change Wallpaper
        Button changeWallpaper = findViewById(R.id.changeWallpaper2);
        changeWallpaper.setOnClickListener(v -> {
            //go to change wallpaper
            intent = new Intent(SettingsUserLogin.this, WallpaperHeader.class);
            startActivity(intent);
        });
        //Selected Langauge
        RelativeLayout selLanguage = findViewById(R.id.selected_language);
        selLanguage.setOnClickListener(v -> {
            intent = new Intent(SettingsUserLogin.this, ChangeLanguage.class);
            startActivity(intent);
        });
        //Selected Theme
        RelativeLayout selTheme = findViewById(R.id.change_theme);
        selTheme.setOnClickListener(v -> {
            //go to change theme
            intent = new Intent(SettingsUserLogin.this, ChangeTheme.class);
            startActivity(intent);
        });
        //Switch WebView to Chrome Custom Tabs
        SwitchMaterial useChrome = findViewById(R.id.switchChrome);
        useChrome.setChecked(new UseChromeShared(this).getEnableChrome());
        useChrome.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs = getSharedPreferences("useChrome", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("useChromeDefault", isChecked);
            editor.apply();
        });
        //Switch Show Welcome
        SwitchMaterial showWelcome = findViewById(R.id.turnoffWelcome);
        showWelcome.setChecked(new WelcomeScreenShared(this).getEnableWelcome());
        showWelcome.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs = getSharedPreferences("welcomeScreen", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("showWelcomeScreen", isChecked);
            editor.apply();
        });
        //Account Settings
        RelativeLayout accountSettings = findViewById(R.id.account_settings);
        accountSettings.setOnClickListener(v -> {
            //go to account settings
            intent = new Intent(SettingsUserLogin.this, AccountSettings.class);
            startActivity(intent);
        });
        //Sign Out account
        Button signOut = findViewById(R.id.sign_out);
        signOut.setOnClickListener(v -> {
            //Show alert dialog
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(R.string.sign_out);
            builder.setView(R.layout.show_signout_text);
            builder.setPositiveButton(R.string.sign_out, (dialog, which) -> {
                //Sign out account
               if (Objects.equals(getUserLogin.getStatus(), "login")) {
                    FirebaseAuth.getInstance().signOut();
                    SaveUserLogined saveUserLogined = new SaveUserLogined(this);
                    saveUserLogined.saveUserLogin("", "", "", "", "", "", "");
                    saveUserLogined.saveBirthday("");
                    saveUserLogined.saveGender("");
                }
                //Push android notification
                NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this, "NewsApp")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.sign_out))
                        .setContentText(getString(R.string.sign_out_success) + ". " + getString(R.string.see_you_again))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                notificationManagerCompat.notify(1, builder1.build());
                //Restart application
                Toast.makeText(this, R.string.sign_out_success, Toast.LENGTH_SHORT).show();
                intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                //Exit dialog
                dialog.dismiss();
            });
            builder.setCancelable(false);
            builder.show();
        });
    }

    private int loadBackground() {
        prefs = getSharedPreferences("Wallpaper", MODE_PRIVATE);
        return prefs.getInt("path", drawerLayout.getBackground().getCurrent().getConstantState().getChangingConfigurations());
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            ActivityOptions.makeSceneTransitionAnimation(SettingsUserLogin.this).toBundle();
            finish();
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        fullName.setText(getUserLogin.getFullName());
        username.setText("@" + getUserLogin.getUsername());
        if (loadBackground() != drawerLayout.getBackground().getCurrent().getConstantState().getChangingConfigurations()) {
            drawerLayout.setBackground(ResourcesCompat.getDrawable(getResources(), loadBackground(), null));
        }
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}