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

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.recycleview.NewsTypeAdapter;
import com.notmiyouji.newsapp.kotlin.recycleviewholder.rss2json.Rss2JsonMultiFeed;
import com.notmiyouji.newsapp.kotlin.util.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.util.CheckConnection;
import com.notmiyouji.newsapp.kotlin.util.NetworkConnection;
import com.notmiyouji.newsapp.kotlin.activity.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.activity.OpenSettingsPage;
import com.notmiyouji.newsapp.kotlin.sharedsettings.AppContextWrapper;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadNavigationHeader;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadThemeShared;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadWallpaperShared;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadWallpaperSharedLogin;

import java.util.Objects;
import java.util.logging.Logger;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Initialization variable
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LinearLayoutManager newsViewLayoutVertical, newsViewLayoutHorizontal;
    private LinearLayout homepageScreen, errorInternet;
    private RecyclerView newsTypeRecyclerView, newsViewHorizontal, newsViewVertical;
    private Toolbar toolbar;
    private NavigationPane navigationPane;
    private LoadWallpaperShared loadWallpaperShared;
    private LoadWallpaperSharedLogin loadWallpaperSharedLogin;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadThemeShared loadThemeShared;
    private GetUserLogin getUserLogin;
    private Rss2JsonMultiFeed feedMultiRSS;
    private EditText searchTextInput;

    private String defaultType = "BreakingNews";

    public String getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //get language from shared preference
        LoadFollowLanguageSystem loadFollowLanguageSystem = new LoadFollowLanguageSystem(newBase);
        super.attachBaseContext(AppContextWrapper.wrap(newBase, Objects.requireNonNull(loadFollowLanguageSystem.getLanguage())));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        TextView welcomeText = findViewById(R.id.welcome_title);
        homepageScreen = findViewById(R.id.homepage_screen);
        errorInternet = findViewById(R.id.noInternetScreen);
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observe(this, isConnected -> {
            if (isConnected) {
                homepageScreen.setVisibility(android.view.View.VISIBLE);
                errorInternet.setVisibility(android.view.View.GONE);
            } else {
                homepageScreen.setVisibility(android.view.View.GONE);
                errorInternet.setVisibility(android.view.View.VISIBLE);
            }
        });
        navigationView = findViewById(R.id.nav_pane_view);
        //From sharedPreference, if user login saved, call navigation pane with user name header
        LoadNavigationHeader loadNavigationHeader = new LoadNavigationHeader(this, navigationView);
        loadNavigationHeader.loadHeader();
        //From SharedPreference, change background for header navigation pane
        getUserLogin = new GetUserLogin(this);
        if (Objects.equals(getUserLogin.getStatus(), "login")) {
            String welcomeUserName = getString(R.string.user_welcome) + " @" + getUserLogin.getUsername() + "\n" + getString(R.string.user_welcome_2);
            welcomeText.setText(welcomeUserName);
            loadWallpaperSharedLogin = new LoadWallpaperSharedLogin(navigationView, this);
            loadWallpaperSharedLogin.loadWallpaper();
        } else {
            welcomeText.setText(R.string.Guest_welcome);
            loadWallpaperShared = new LoadWallpaperShared(navigationView, this);
            loadWallpaperShared.loadWallpaper();
        }

        //open sign in page from navigation view
        if (Objects.equals(getUserLogin.getStatus(), "")) {
            CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
            callSignInForm.callSignInForm();
        }
        drawerLayout = findViewById(R.id.home_page);
        toolbar = findViewById(R.id.nav_button);
        newsTypeRecyclerView = findViewById(R.id.news_type);
        searchTextInput = findViewById(R.id.search_input);
        newsViewHorizontal = findViewById(R.id.cardnews_view_horizontal);
        newsViewVertical = findViewById(R.id.cardnews_view_vertical);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        navigationPane = new NavigationPane(drawerLayout, this, toolbar, navigationView, R.id.home_menu);
        navigationPane.CallFromUser();
        //From SharedPreference, call back source name
        reloadData();
        //NewsCategory Type List
        loadCategory();
        CheckConnection checkConnection = new CheckConnection();
        if (checkConnection.checkConnection(this)) {
            //Load news from source
            LoadSourceNews(getDefaultType());
        }
        //Refresh news
        swipeRefreshLayout.setOnRefreshListener(() -> {
            LoadSourceNews(getDefaultType());
            swipeRefreshLayout.setRefreshing(false);
        });
        //Search the news by keyword after click search button
        searchTextInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == 6) {
                //Hide newsViewHorizontal and Category List
                newsTypeRecyclerView.setVisibility(android.view.View.GONE);
                newsViewHorizontal.setVisibility(android.view.View.GONE);
                searchNewsByKeyWord(getUserLogin.getUserID(), searchTextInput.getText().toString());
            }
            //if the edit text is empty, enable back newsViewHorizontal load news from source
            if (searchTextInput.getText().toString().equals("")) {
                newsTypeRecyclerView.setVisibility(android.view.View.VISIBLE);
                newsViewHorizontal.setVisibility(android.view.View.VISIBLE);
                LoadSourceNews(getDefaultType());
            }
            return false;
        });
    }

    private void searchNewsByKeyWord(String userId, String keyWord) {
        MaterialAltertLoading materialAltertLoading = new MaterialAltertLoading(this);
        MaterialAlertDialogBuilder mDialog = materialAltertLoading.getDialog();
        AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
        //Load the news found in vertical RecyclerView
        Thread loadViewVertical = new Thread(() -> {
            newsViewLayoutVertical = new LinearLayoutManager(HomePage.this, LinearLayoutManager.VERTICAL, false);
            feedMultiRSS = new Rss2JsonMultiFeed(HomePage.this, newsViewVertical, newsViewLayoutVertical);
            feedMultiRSS.clearAdapterVertical();
            feedMultiRSS.clearAdapterHorizontal();
            feedMultiRSS.rss2JsonVerticalByKeyWord(userId, keyWord, getDefaultType(), alertDialog);
        });
        loadViewVertical.start();
        //After load news, close the dialog if two thread is done
        try {
            loadViewVertical.join();
        } catch (InterruptedException e) {
            Logger.getLogger("Error").warning(e.getMessage());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCategory() {
        NewsTypeAdapter newsTypeAdapter = new NewsTypeAdapter(this, getUserLogin.getUserID());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        newsTypeRecyclerView.setLayoutManager(linearLayoutManager);
        newsTypeRecyclerView.setAdapter(newsTypeAdapter);
        newsTypeAdapter.notifyDataSetChanged();
    }

    private void LoadSourceNews(String type) {
        MaterialAltertLoading materialAltertLoading = new MaterialAltertLoading(this);
        MaterialAlertDialogBuilder mDialog = materialAltertLoading.getDialog();
        AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
        newsViewHorizontal.setVisibility(View.VISIBLE);
        newsViewVertical.setVisibility(View.VISIBLE);
        //Load Latest News Horizontal
        Thread loadViewHorizontal = new Thread(() -> {
            newsViewLayoutHorizontal = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
            feedMultiRSS = new Rss2JsonMultiFeed(HomePage.this, newsViewHorizontal, newsViewLayoutHorizontal);
            feedMultiRSS.rss2JsonHorizontal(getUserLogin.getUserID(), type, alertDialog);
        });
        loadViewHorizontal.start();
        //Load Latest News Vertical
        Thread loadViewVertical = new Thread(() -> {
            newsViewLayoutVertical = new LinearLayoutManager(HomePage.this, LinearLayoutManager.VERTICAL, false);
            feedMultiRSS = new Rss2JsonMultiFeed(HomePage.this, newsViewVertical, newsViewLayoutVertical);
            feedMultiRSS.rss2JsonVertical(getUserLogin.getUserID(), type, alertDialog);
        });
        loadViewVertical.start();
        //After load news, close the dialog if two thread is done
        try {
            loadViewHorizontal.join();
            loadViewVertical.join();
        } catch (InterruptedException e) {
            Logger.getLogger("Error").warning(e.getMessage());
        }
    }

    private void reloadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SourceName", MODE_PRIVATE);
        if (!sharedPreferences.getString("name", "").equals(getDefaultType())) {
            setDefaultType(sharedPreferences.getString("name", getDefaultType()));
        }
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                ActivityOptions.makeSceneTransitionAnimation(HomePage.this).toBundle();
                finish();
            }
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        int menuItem = item.getItemId();
        switch (menuItem) {
            case R.id.newsapi_menu -> {
                intent = new Intent(HomePage.this, NewsApiPage.class);
                startActivity(intent);
                this.finish();
            }
            case R.id.source_menu -> {
                intent = new Intent(HomePage.this, SourceNewsList.class);
                startActivity(intent);
                this.finish();
            }
            case R.id.favourite_menu -> {
                intent = new Intent(HomePage.this, FavouriteNews.class);
                startActivity(intent);
                this.finish();
            }
            case R.id.settings_menu -> {
                OpenSettingsPage openSettingsPage = new OpenSettingsPage(HomePage.this);
                openSettingsPage.openSettings();
            }
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        if (Objects.equals(getUserLogin.getStatus(), "login")) {
            loadWallpaperSharedLogin = new LoadWallpaperSharedLogin(navigationView, this);
            loadWallpaperSharedLogin.loadWallpaper();
        } else {
            loadWallpaperShared = new LoadWallpaperShared(navigationView, this);
            loadWallpaperShared.loadWallpaper();
        }
        navigationPane = new NavigationPane(drawerLayout, this, toolbar, navigationView, R.id.home_menu);
        navigationPane.CallFromUser();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}