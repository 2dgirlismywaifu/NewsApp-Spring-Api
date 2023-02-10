package com.notmiyouji.newsapp.java.Signed;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Global.AboutApplication;
import com.notmiyouji.newsapp.java.Global.ChangeLanguage;
import com.notmiyouji.newsapp.java.Global.ChangeTheme;
import com.notmiyouji.newsapp.java.Global.WallpaperHeader;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadThemeShared;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.UseChromeShared;
import com.notmiyouji.newsapp.kotlin.SharedSettings.WelcomeScreenShared;

public class SettingsLogined extends AppCompatActivity {
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    LoadThemeShared loadThemeShared;
    RelativeLayout aboutApp, selLanguage, selTheme, accountSettings;
    int menu;
    Button signOut;
    TextView fullName, username;
    DrawerLayout drawerLayout;
    SwitchMaterial useChrome, showWelcome;
    Intent intent;
    SharedPreferences prefs;
    GetUserLogined getUserLogined;

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
        getUserLogined = new GetUserLogined(this);
        fullName.setText(getUserLogined.getFullname());
        username.setText("@" + getUserLogined.getUsername());
        //Set Background form SharedPreference
        drawerLayout = findViewById(R.id.settings_banner_logined);
        if (loadBackground() != drawerLayout.getBackground().getCurrent().getConstantState().getChangingConfigurations()) {
            drawerLayout.setBackground(ResourcesCompat.getDrawable(getResources(), loadBackground(), null));
        }
        //About Application
        aboutApp = findViewById(R.id.about_application);
        aboutApp.setOnClickListener(v -> {
            //go to about application
            intent = new Intent(SettingsLogined.this, AboutApplication.class);
            startActivity(intent);
        });
        //Load avatar
        ShapeableImageView avatar = findViewById(R.id.avatar_user_logined);
        LoadImageURL loadImageURL = new LoadImageURL(getUserLogined.getAvatar());
        loadImageURL.loadImageUser(avatar);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Change Wallpaper
        Button changeWallpaper = findViewById(R.id.changeWallpaper2);
        changeWallpaper.setOnClickListener(v -> {
            //go to change wallpaper
            intent = new Intent(SettingsLogined.this, WallpaperHeader.class);
            startActivity(intent);
        });
        //Selected Langauge
        selLanguage = findViewById(R.id.selected_language);
        selLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SettingsLogined.this, ChangeLanguage.class);
                startActivity(intent);
            }
        });
        //Selected Theme
        selTheme = findViewById(R.id.change_theme);
        selTheme.setOnClickListener(v -> {
            //go to change theme
            intent = new Intent(SettingsLogined.this, ChangeTheme.class);
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
        //Account Settings
        accountSettings = findViewById(R.id.account_settings);
        accountSettings.setOnClickListener(v -> {
            //go to account settings
            intent = new Intent(SettingsLogined.this, AccountSettings.class);
            startActivity(intent);
        });
        //Sign Out account
        signOut = findViewById(R.id.sign_out);
        signOut.setOnClickListener(v -> {
            //Show alert dialog
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle(R.string.sign_out);
            builder.setView(R.layout.show_signout_text);
            builder.setMessage(R.string.sign_out_message);
            builder.setPositiveButton(R.string.sign_out, (dialog, which) -> {
                //Sign out account
                if (getUserLogined.getStatus().equals("google")) {
                    GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                            build()).signOut();
                    FirebaseAuth.getInstance().signOut();
                    SaveUserLogined saveUserLogined = new SaveUserLogined(this);
                    saveUserLogined.saveUserLogined("", "", "", "", "", "","");
                    saveUserLogined.saveBirthday("");
                    saveUserLogined.saveGender("");
                }
                else if (getUserLogined.getStatus().equals("login")) {
                    FirebaseAuth.getInstance().signOut();
                    SaveUserLogined saveUserLogined = new SaveUserLogined(this);
                    saveUserLogined.saveUserLogined("", "", "", "", "", "","");
                    saveUserLogined.saveBirthday("");
                    saveUserLogined.saveGender("");
                }
                //Push android notification
                NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this, "NewsApp")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.sign_out))
                        .setContentText(getString(R.string.sign_out_success) +". " + getString(R.string.see_you_again))
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();
        fullName.setText(getUserLogined.getFullname());
        username.setText("@" + getUserLogined.getUsername());
        if (loadBackground() != drawerLayout.getBackground().getCurrent().getConstantState().getChangingConfigurations()) {
            drawerLayout.setBackground(ResourcesCompat.getDrawable(getResources(), loadBackground(), null));
        }
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}