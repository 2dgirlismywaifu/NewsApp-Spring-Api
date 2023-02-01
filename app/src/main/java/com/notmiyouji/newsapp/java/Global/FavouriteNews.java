package com.notmiyouji.newsapp.java.Global;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.NewsAPI.NewsAPIPage;
import com.notmiyouji.newsapp.java.RSSURL.HomePage;
import com.notmiyouji.newsapp.java.RSSURL.SourceNewsList;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.OpenSettingsPage;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadNavigationHeader;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadWallpaperShared;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadWallpaperSharedLogined;

public class FavouriteNews extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Initialization variable
    DrawerLayout drawerFavourtie;
    NavigationView navigationView;
    Toolbar toolbar;
    NavigationPane navigationPane;
    GetUserLogined getUserLogined;
    Intent intent;
    LoadWallpaperShared loadWallpaperShared;
    LoadWallpaperSharedLogined loadWallpaperSharedLogined;
    LoadFollowLanguageSystem loadFollowLanguageSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_news);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        navigationView = findViewById(R.id.nav_pane_favourite_news);
        //From sharedPreference, if user logined saved, call navigation pane with user name header
        LoadNavigationHeader loadNavigationHeader = new LoadNavigationHeader(this, navigationView);
        loadNavigationHeader.loadHeader();
        //From SharedPreference, change background for header navigation pane
        getUserLogined = new GetUserLogined(this);
        if (getUserLogined.getStatus().equals("login")) {
            loadWallpaperSharedLogined = new LoadWallpaperSharedLogined(navigationView, this);
            loadWallpaperSharedLogined.loadWallpaper();
        } else {
            loadWallpaperShared = new LoadWallpaperShared(navigationView, this);
            loadWallpaperShared.loadWallpaper();
        }
        drawerFavourtie = findViewById(R.id.favourite_news_page);
        toolbar = findViewById(R.id.nav_button);
        navigationPane = new NavigationPane(drawerFavourtie, this, toolbar, navigationView, R.id.favourite_menu);
        navigationPane.CallFromUser();
        //open sign in page from navigationview
        if (!getUserLogined.getStatus().equals("login")) {
            CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
            callSignInForm.callSignInForm();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerFavourtie.isDrawerOpen(GravityCompat.START)) {
            drawerFavourtie.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int menuitem = item.getItemId();
        if (menuitem == R.id.home_menu) {
            intent = new Intent(FavouriteNews.this, HomePage.class);
            startActivity(intent);
            this.finish();
        } else if (menuitem == R.id.source_menu) {
            intent = new Intent(FavouriteNews.this, SourceNewsList.class);
            startActivity(intent);
            this.finish();
        } else if (menuitem == R.id.newsapi_menu) {
            intent = new Intent(FavouriteNews.this, NewsAPIPage.class);
            startActivity(intent);
            this.finish();
        } else if (menuitem == R.id.settings_menu) {
            OpenSettingsPage openSettingsPage = new OpenSettingsPage(FavouriteNews.this);
            openSettingsPage.openSettings();
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        //From SharedPreference, change background for header navigation pane
        if (getUserLogined.getStatus().equals("login")) {
            loadWallpaperSharedLogined.loadWallpaper();
        } else {
            loadWallpaperShared.loadWallpaper();
        }
        navigationPane = new NavigationPane(drawerFavourtie, this, toolbar, navigationView, R.id.favourite_menu);
        navigationPane.CallFromUser();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
    }
}