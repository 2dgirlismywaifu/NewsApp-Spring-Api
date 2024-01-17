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
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import com.notmiyouji.newsapp.java.recycleview.ListSourceAdapter;
import com.notmiyouji.newsapp.java.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.CheckNetworkConnection;
import com.notmiyouji.newsapp.kotlin.NetworkConnection;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.activity.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.activity.OpenSettingsPage;
import com.notmiyouji.newsapp.kotlin.model.NewsAppResult;
import com.notmiyouji.newsapp.kotlin.model.NewsSource;
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

public class SourceNewsList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Initialization variable
    private DrawerLayout drawerSourceNews;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private NavigationPane navigationPane;
    private RecyclerView recyclerView;
    private ListSourceAdapter listSourceAdapter;
    private LoadWallpaperShared loadWallpaperShared;
    private LoadWallpaperSharedLogin loadWallpaperSharedLogin;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final NewsAppInterface newsAPPInterface = NewsAppApi.getAPIClient().create(NewsAppInterface.class);
    private List<NewsSource> newsSources = new ArrayList<>();
    private LoadFollowLanguageSystem loadFollowLanguageSystem;
    private LoadThemeShared loadThemeShared;
    private GetUserLogin getUserLogin;
    private LinearLayout sourceListPage, errorPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_news_list);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        sourceListPage = findViewById(R.id.sourceListPage);
        errorPage = findViewById(R.id.noInternetScreen);
        //Check Internet Connection
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observe(this, isConnected -> {
            if (isConnected) {
                sourceListPage.setVisibility(LinearLayout.VISIBLE);
                errorPage.setVisibility(LinearLayout.GONE);
            } else {
                sourceListPage.setVisibility(LinearLayout.GONE);
                errorPage.setVisibility(LinearLayout.VISIBLE);
            }
        });
        navigationView = findViewById(R.id.nav_pane_sourceList);
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
        drawerSourceNews = findViewById(R.id.source_news_page);
        toolbar = findViewById(R.id.nav_button);
        recyclerView = findViewById(R.id.sources_list);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        navigationPane = new NavigationPane(drawerSourceNews, this, toolbar, navigationView, R.id.source_menu);
        navigationPane.CallFromUser();
        //open sign in page from navigation view
        if (Objects.equals(getUserLogin.getStatus(), "")) {
            CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
            callSignInForm.callSignInForm();
        }
        CheckNetworkConnection checkNetworkConnection = new CheckNetworkConnection();
        if (checkNetworkConnection.checkConnection(this)) {
            //Recycle View
            loadSourceList(this);
        }
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadSourceList(SourceNewsList.this);
            swipeRefreshLayout.setRefreshing(false);
        });
        //Recycle View Filter
        EditText searchSource = findViewById(R.id.search_input);
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

    public void filter(String s) {
        List<NewsSource> newsSourceList = new ArrayList<>();
        for (NewsSource newsSource : newsSources) {
            if (Objects.requireNonNull(
                    newsSource.getSourceName()).toLowerCase().contains(s.toLowerCase())) {
                newsSourceList.add(newsSource);
            }
        }
        listSourceAdapter.filterList(newsSourceList);
    }

    public void loadSourceList(AppCompatActivity activity) {
        MaterialAltertLoading materialAltertLoading = new MaterialAltertLoading(this);
        MaterialAlertDialogBuilder mDialog = materialAltertLoading.getDialog();
        AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
        Thread loadSource = new Thread(() -> {
            Call<NewsAppResult> call;
            if (getUserLogin.getUserID() != null  && !getUserLogin.getUserID().isEmpty()) {
                call = newsAPPInterface.accountAllSource(Objects.requireNonNull(getUserLogin.getUserID()));
            } else {
                call = newsAPPInterface.guestAllSource();
            }
            assert call != null;
            call.enqueue(new retrofit2.Callback<>() {
                @Override
                public void onResponse(@NonNull Call<NewsAppResult> call, @NonNull retrofit2.Response<NewsAppResult> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getNewsSource() != null) {
                            if (!newsSources.isEmpty()) {
                                newsSources.clear();
                            }
                            newsSources = response.body().getNewsSource();
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            listSourceAdapter = new ListSourceAdapter(activity, newsSources);
                            recyclerView.setAdapter(listSourceAdapter);
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

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (drawerSourceNews.isDrawerOpen(GravityCompat.START)) {
                drawerSourceNews.closeDrawer(GravityCompat.START);
            } else {
                ActivityOptions.makeSceneTransitionAnimation(SourceNewsList.this).toBundle();
                finish();
            }
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int menuItem = item.getItemId();
        Intent intent;
        if (menuItem == R.id.home_menu) {
            intent = new Intent(this, HomePage.class);
            startActivity(intent);
            finish();
        } else if (menuItem == R.id.newsapi_menu) {
            intent = new Intent(this, NewsApiPage.class);
            startActivity(intent);
            finish();
        } else if (menuItem == R.id.favourite_menu) {
            intent = new Intent(this, FavouriteNews.class);
            startActivity(intent);
            finish();
        } else if (menuItem == R.id.settings_menu) {
            OpenSettingsPage openSettingsPage = new OpenSettingsPage(this);
            openSettingsPage.openSettings();
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
        navigationPane = new NavigationPane(drawerSourceNews, this, toolbar, navigationView, R.id.source_menu);
        navigationPane.CallFromUser();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}