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

package com.notmiyouji.newsapp.java.rssurl;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.general.FavouriteNews;
import com.notmiyouji.newsapp.java.general.MaterialAltertLoading;
import com.notmiyouji.newsapp.java.general.NavigationPane;
import com.notmiyouji.newsapp.java.newsapi.NewsApiPage;
import com.notmiyouji.newsapp.java.recycleview.NewsTypeAdapter;
import com.notmiyouji.newsapp.java.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.java.rss2json.Rss2JsonMultiFeed;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.CheckNetworkConnection;
import com.notmiyouji.newsapp.kotlin.NetworkConnection;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.Utils;
import com.notmiyouji.newsapp.kotlin.activity.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.activity.OpenSettingsPage;
import com.notmiyouji.newsapp.kotlin.model.NewsAppResult;
import com.notmiyouji.newsapp.kotlin.model.NewsSource;
import com.notmiyouji.newsapp.kotlin.sharedsettings.AppContextWrapper;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadNavigationHeader;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadThemeShared;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadWallpaperShared;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadWallpaperSharedLogin;
import com.notmiyouji.newsapp.kotlin.sharedsettings.SharedPreferenceSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import retrofit2.Call;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final NewsAppInterface newsAPPInterface = NewsAppApi.getAPIClient().create(NewsAppInterface.class);
    //Initialization variable
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LinearLayoutManager newsViewLayoutVertical, newsViewLayoutHorizontal;
    private LinearLayout homepageScreen, errorInternet;
    private RecyclerView recyclerView, newsViewHorizontal, newsViewVertical;
    private Toolbar toolbar;
    private NavigationPane navigationPane;
    private LoadWallpaperShared loadWallpaperShared;
    private LoadWallpaperSharedLogin loadWallpaperSharedLogin;
    private ExtendedFloatingActionButton filterSource;
    private TextView chooseTitle;
    private TextInputLayout chooseHint;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<NewsSource> newsSources = new ArrayList<>();
    private LoadThemeShared loadThemeShared;
    private GetUserLogin getUserLogin;
    private CheckBox syncSubscribe;
    private MaterialAutoCompleteTextView spinnerRss;
    private Rss2JsonMultiFeed feedMultiRSS;

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
        filterSource = findViewById(R.id.filterSource);
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observe(this, isConnected -> {
            if (isConnected) {
                homepageScreen.setVisibility(android.view.View.VISIBLE);
                filterSource.setVisibility(android.view.View.VISIBLE);
                errorInternet.setVisibility(android.view.View.GONE);
            } else {
                homepageScreen.setVisibility(android.view.View.GONE);
                filterSource.setVisibility(android.view.View.GONE);
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
        drawerLayout = findViewById(R.id.home_page);
        toolbar = findViewById(R.id.nav_button);
        recyclerView = findViewById(R.id.news_type);
        newsViewHorizontal = findViewById(R.id.cardnews_view_horizontal);
        newsViewVertical = findViewById(R.id.cardnews_view_vertical);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        navigationPane = new NavigationPane(drawerLayout, this, toolbar, navigationView, R.id.home_menu);
        navigationPane.CallFromUser();
        //From SharedPreference, call back source name
        reloadData();
        //NewsCategory Type List
        loadCategory(getDefaultType());
        //open sign in page from navigation view
        if ("".equals(getUserLogin.getStatus())) {
            CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
            callSignInForm.callSignInForm();
        }
        CheckNetworkConnection checkNetworkConnection = new CheckNetworkConnection();
        if (checkNetworkConnection.checkConnection(this)) {
            //Load news from source
            LoadSourceNews(getDefaultType());
        }
        //User progress bar
        //Hide float button when scroll recyclerview vertical
        hideWhenScroll();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            LoadSourceNews(getDefaultType());
            swipeRefreshLayout.setRefreshing(false);
        });
    }


    //Collapse float button
    private void hideWhenScroll() {
        NestedScrollView homepageScroll = findViewById(R.id.homepageScroll);
        homepageScroll.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (homepageScroll.getScrollY() > 0) {
                filterSource.shrink();
            } else {
                filterSource.extend();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCategory(String type) {
        NewsTypeAdapter newsTypeAdapter = new NewsTypeAdapter(this, getUserLogin.getUserID());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsTypeAdapter);
        newsTypeAdapter.notifyDataSetChanged();
    }

    private void LoadSourceNews(String type) {
        MaterialAltertLoading materialAltertLoading = new MaterialAltertLoading(this);
        MaterialAlertDialogBuilder mDialog = materialAltertLoading.getDiaglog();
        AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
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

    @Deprecated //This function is not used anymore
    private void openSourceChoose() {
        filterSource.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomePage.this);
            bottomSheetDialog.setContentView(R.layout.choose_feed);
            bottomSheetDialog.show();
            spinnerRss = bottomSheetDialog.findViewById(R.id.spinner_rss);
            chooseTitle = bottomSheetDialog.findViewById(R.id.choose_title);
            chooseHint = bottomSheetDialog.findViewById(R.id.hint_to_choose);
            syncSubscribe = bottomSheetDialog.findViewById(R.id.checkSubscrible);
            chooseTitle.setText(R.string.choose_rss);
            chooseHint.setHint(R.string.Select_RSS_Feed);
            assert spinnerRss != null;
            assert syncSubscribe != null;
            //if checkbox is checked, load source from user account
            syncSubscribe.setOnClickListener(v1 -> {
                if (syncSubscribe.isChecked()) {
                    String statusAccount = getUserLogin.getStatus();
                    if (statusAccount != null) {
                        if (statusAccount.equals("login")) {
                            spinnerRss.setText("");
                            AccountSourceList(spinnerRss, getUserLogin.getUserID());
                        } else {
                            syncSubscribe.setChecked(false);
                            Toast.makeText(HomePage.this, R.string.please_login_to_use_this_feature, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    spinnerRss.setText("");
                    GuestSourceList(spinnerRss);
                }
            });
            spinnerRss.setText("");
            GuestSourceList(spinnerRss);
            Button okBtn = bottomSheetDialog.findViewById(R.id.btnLoad);
            assert okBtn != null;
            okBtn.setOnClickListener(v1 -> {
                String sourceName = spinnerRss.getText().toString();
                if (sourceName.isEmpty()) {
                    Toast.makeText(HomePage.this, R.string.source_not_choose, Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferenceSettings sharedPreferenceSettings = new SharedPreferenceSettings(HomePage.this);
                    sharedPreferenceSettings.getSharedSource(sourceName);
                    setDefaultType(sourceName);
                    bottomSheetDialog.dismiss();
                    loadCategory(sourceName);
                    LoadSourceNews(sourceName);
                }

            });
        });
    }
    @Deprecated
    private void GuestSourceList(MaterialAutoCompleteTextView spinner_rss) {
        Call<NewsAppResult> call = newsAPPInterface.guestAllSource();
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
                        List<NewsSource> newsSourceList = newsSources;
                        ArrayList<String> listSource = new ArrayList<>();
                        for (NewsSource newsSource : newsSourceList) {
                            listSource.add(newsSource.getSourceName());
                        }
                        assert spinner_rss != null;
                        spinner_rss.setAdapter(new ArrayAdapter<>(HomePage.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listSource));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                Logger.getLogger("Error").warning(t.getMessage());
            }
        });
    }
    @Deprecated
    private void AccountSourceList(MaterialAutoCompleteTextView spinner_rss, String userid) {
        Call<NewsAppResult> call = newsAPPInterface.accountAllSource(Utils.encodeToBase64(userid));
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
                        List<NewsSource> newsSourceList = newsSources;
                        ArrayList<String> listSource = new ArrayList<>();
                        for (NewsSource newsSource : newsSourceList) {
                            listSource.add(newsSource.getSourceName());
                        }
                        //toast message if listSource is empty
                        if (listSource.isEmpty()) {
                            Toast.makeText(HomePage.this, R.string.no_source, Toast.LENGTH_SHORT).show();
                        }
                        assert spinner_rss != null;
                        spinner_rss.setAdapter(new ArrayAdapter<>(HomePage.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listSource));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                Logger.getLogger("Error").warning(t.getMessage());
            }
        });
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