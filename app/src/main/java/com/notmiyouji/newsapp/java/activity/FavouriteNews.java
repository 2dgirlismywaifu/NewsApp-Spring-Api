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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.notmiyouji.newsapp.java.activity.userlogin.SignInForm;
import com.notmiyouji.newsapp.java.recycleview.NewsFavouriteAdapter;
import com.notmiyouji.newsapp.java.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.CheckNetworkConnection;
import com.notmiyouji.newsapp.kotlin.NetworkConnection;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.Utils;
import com.notmiyouji.newsapp.kotlin.activity.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.activity.OpenSettingsPage;
import com.notmiyouji.newsapp.kotlin.model.NewsAppResult;
import com.notmiyouji.newsapp.kotlin.model.NewsFavourite;
import com.notmiyouji.newsapp.kotlin.sharedsettings.AppContextWrapper;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadNavigationHeader;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadThemeShared;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadWallpaperShared;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadWallpaperSharedLogin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import retrofit2.Call;

public class FavouriteNews extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Initialization variable
    private DrawerLayout drawerFavourite;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private NavigationPane navigationPane;
    private GetUserLogin getUserLogin;
    private Intent intent;
    private LoadWallpaperShared loadWallpaperShared;
    private LoadWallpaperSharedLogin loadWallpaperSharedLogin;
    private LoadThemeShared loadThemeShared;
    private final NewsAppInterface newsAppInterface = NewsAppApi.getAPIClient().create(NewsAppInterface.class);
    private List<NewsFavourite> newsFavourite = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private NewsFavouriteAdapter newsFavouriteAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout errorPage, favouritePage, requestLogin;

    protected void attachBaseContext(Context newBase) {
        //get language from shared preference
        LoadFollowLanguageSystem loadFollowLanguageSystem = new LoadFollowLanguageSystem(newBase);
        super.attachBaseContext(AppContextWrapper.wrap(newBase, Objects.requireNonNull(loadFollowLanguageSystem.getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_news);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        Button signInButton = findViewById(R.id.SignInBtn);
        favouritePage = findViewById(R.id.relativeLayout);
        errorPage = findViewById(R.id.noInternetScreen);
        requestLogin = findViewById(R.id.requiredLogin);
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observe(this, isConnected -> {
            if (isConnected) {
                favouritePage.setVisibility(android.view.View.VISIBLE);
                errorPage.setVisibility(android.view.View.GONE);
            } else {
                favouritePage.setVisibility(android.view.View.GONE);
                requestLogin.setVisibility(android.view.View.GONE);
                errorPage.setVisibility(android.view.View.VISIBLE);
            }
        });
        recyclerView = findViewById(R.id.cardnews_view_vertical);
        navigationView = findViewById(R.id.nav_pane_favourite_news);
        //From sharedPreference, if user logined saved, call navigation pane with user name header
        LoadNavigationHeader loadNavigationHeader = new LoadNavigationHeader(this, navigationView);
        loadNavigationHeader.loadHeader();
        //From SharedPreference, change background for header navigation pane
        getUserLogin = new GetUserLogin(this);
        if (Objects.equals(getUserLogin.getStatus(), "login")) {
            loadWallpaperSharedLogin = new LoadWallpaperSharedLogin(navigationView, this);
            loadWallpaperSharedLogin.loadWallpaper();
        } else {
            loadWallpaperShared = new LoadWallpaperShared(navigationView, this);
            loadWallpaperShared.loadWallpaper();
        }
        drawerFavourite = findViewById(R.id.favourite_news_page);
        toolbar = findViewById(R.id.nav_button);
        navigationPane = new NavigationPane(drawerFavourite, this, toolbar, navigationView, R.id.favourite_menu);
        navigationPane.CallFromUser();
        //open sign in page from navigation view
        if (Objects.equals(getUserLogin.getStatus(), "")) {
            CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
            callSignInForm.callSignInForm();
        }
        CheckNetworkConnection checkNetworkConnection = new CheckNetworkConnection();
        if (checkNetworkConnection.checkConnection(this)) {
            if (getUserLogin.getStatus().equals("login")) {
                loadNewsFavourite(this);
            } else {
                favouritePage.setVisibility(View.GONE);
                requestLogin.setVisibility(View.VISIBLE);
                Toast.makeText(this, R.string.please_login_to_see_your_favourite_news, Toast.LENGTH_SHORT).show();
            }
        }
        //Sign in button
        signInButton.setOnClickListener(v -> {
            intent = new Intent(this, SignInForm.class);
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent);
        });
        //Swipe to refresh
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (getUserLogin.getStatus().equals("login")) {
                loadNewsFavourite(this);
            } else {
                Toast.makeText(this, R.string.please_login_to_see_your_favourite_news, Toast.LENGTH_SHORT).show();
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        //Recycle View Filter
        EditText searchSource = findViewById(R.id.search_news);
        searchSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void loadNewsFavourite(AppCompatActivity activity) {
        MaterialAltertLoading materialAltertLoading = new MaterialAltertLoading(this);
        MaterialAlertDialogBuilder mDialog = materialAltertLoading.getDialog();
        AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
        Thread loadSource = new Thread(() -> {
            Call<NewsAppResult> favouriteShowNews = newsAppInterface.showNewsFavouriteByUserId(
                    Utils.encodeToBase64(Objects.requireNonNull(getUserLogin.getUserID())));
            assert favouriteShowNews != null;
            favouriteShowNews.enqueue(new retrofit2.Callback<>() {
                @Override
                public void onResponse(@NonNull Call<NewsAppResult> call, @NonNull retrofit2.Response<NewsAppResult> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getNewsFavouriteList() != null) {
                            if (!newsFavourite.isEmpty()) {
                                newsFavourite.clear();
                            }
                            newsFavourite = response.body().getNewsFavouriteList();
                            linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            newsFavouriteAdapter = new NewsFavouriteAdapter(newsFavourite, activity);
                            recyclerView.setAdapter(newsFavouriteAdapter);
                            runOnUiThread(() -> recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(alertDialog::dismiss));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                    Logger.getLogger("Error").warning(t.getMessage());
                }
            });

        });
        loadSource.start();
    }

    public void filter(String s) {
        List<NewsFavourite> newsFavouriteList = new ArrayList<>();
        for (NewsFavourite item : newsFavourite) {
            if (Objects.requireNonNull(
                    item.getTitle()).toLowerCase().contains(s.toLowerCase())) {
                newsFavouriteList.add(item);
            }
        }
        newsFavouriteAdapter.filterList(newsFavouriteList);
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (drawerFavourite.isDrawerOpen(GravityCompat.START)) {
                drawerFavourite.closeDrawer(GravityCompat.START);
            } else {
                ActivityOptions.makeSceneTransitionAnimation(FavouriteNews.this).toBundle();
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
        int menuItem = item.getItemId();
        switch (menuItem) {
            case R.id.home_menu -> {
                intent = new Intent(this, HomePage.class);
                startActivity(intent);
                finish();
            }
            case R.id.source_menu -> {
                intent = new Intent(this, SourceNewsList.class);
                startActivity(intent);
                finish();
            }
            case R.id.newsapi_menu -> {
                intent = new Intent(this, NewsApiPage.class);
                startActivity(intent);
                finish();
            }
            case R.id.settings_menu -> {
                OpenSettingsPage openSettingsPage = new OpenSettingsPage(this);
                openSettingsPage.openSettings();
            }
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        //From SharedPreference, change background for header navigation pane
        if (Objects.equals(getUserLogin.getStatus(), "login")) {
            loadWallpaperSharedLogin = new LoadWallpaperSharedLogin(navigationView, this);
            loadWallpaperSharedLogin.loadWallpaper();
        } else {
            loadWallpaperShared = new LoadWallpaperShared(navigationView, this);
            loadWallpaperShared.loadWallpaper();
        }
        navigationPane = new NavigationPane(drawerFavourite, this, toolbar, navigationView, R.id.favourite_menu);
        navigationPane.CallFromUser();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }


}