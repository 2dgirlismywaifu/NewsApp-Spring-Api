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

package com.notmiyouji.newsapp.java.newsapi;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.notmiyouji.newsapp.java.activity.FavouriteNews;
import com.notmiyouji.newsapp.java.activity.HomePage;
import com.notmiyouji.newsapp.java.activity.MaterialAltertLoading;
import com.notmiyouji.newsapp.java.activity.NavigationPane;
import com.notmiyouji.newsapp.java.activity.SourceNewsList;
import com.notmiyouji.newsapp.java.category.NewsApiCategory;
import com.notmiyouji.newsapp.java.recycleview.NewsAPITypeAdapter;
import com.notmiyouji.newsapp.java.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.CheckNetworkConnection;
import com.notmiyouji.newsapp.kotlin.NetworkConnection;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.Utils;
import com.notmiyouji.newsapp.kotlin.activity.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.activity.OpenSettingsPage;
import com.notmiyouji.newsapp.kotlin.model.NewsAppResult;
import com.notmiyouji.newsapp.kotlin.newsapi.Article;
import com.notmiyouji.newsapp.kotlin.newsapi.Country;
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
import retrofit2.Callback;
import retrofit2.Response;

public class NewsApiPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final NewsAppInterface newsAppInterface = NewsAppApi.getAPIClient().create(NewsAppInterface.class);
    private final NewsApiCategory newsAPICategory = new NewsApiCategory();
    //Initialization variable
    private DrawerLayout drawerNewsAPI;
    private NavigationView navigationView;
    private LinearLayoutManager newsAPIHorizontalLayout;
    private RecyclerView newsTypeView, newsViewHorizontal, newsViewVertical;
    private Toolbar toolbar;
    private NavigationPane navigationPane;
    private List<Article> articles = new ArrayList<>(); //not include category
    private NewsAdapterHorizontal newsAdapterHorizontal;
    private Call<NewsAppResult> call;
    private LoadWallpaperShared loadWallpaperShared;
    private LoadWallpaperSharedLogin loadWallpaperSharedLogin;
    private ExtendedFloatingActionButton filterCountry;
    private TextView chooseTitle;
    private TextInputLayout chooseHint;
    private List<Country> countryList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadThemeShared loadThemeShared;
    private GetUserLogin getUserLogin;
    private LinearLayout newsApiPage, errorPage;
    private String countryCodeDefault = "us";

    public String getCountryCodeDefault() {
        return countryCodeDefault;
    }

    public void setCountryCodeDefault(String countryCodeDefault) {
        this.countryCodeDefault = countryCodeDefault;
    }

    protected void attachBaseContext(Context newBase) {
        //get language from shared preference
        LoadFollowLanguageSystem loadFollowLanguageSystem = new LoadFollowLanguageSystem(newBase);
        String language = loadFollowLanguageSystem.getLanguage();
        assert language != null;
        super.attachBaseContext(AppContextWrapper.wrap(newBase, language));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_api_page);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        filterCountry = findViewById(R.id.filterCountry);
        newsApiPage = findViewById(R.id.newsAPIPage);
        errorPage = findViewById(R.id.noInternetScreen);
        //Check internet connection
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observe(this, isConnected -> {
            if (isConnected) {
                newsApiPage.setVisibility(android.view.View.VISIBLE);
                filterCountry.setVisibility(android.view.View.VISIBLE);
                errorPage.setVisibility(android.view.View.GONE);
            } else {
                newsApiPage.setVisibility(android.view.View.GONE);
                filterCountry.setVisibility(android.view.View.GONE);
                errorPage.setVisibility(android.view.View.VISIBLE);
            }
        });
        navigationView = findViewById(R.id.nav_pane_newsapi);
        //From sharedPreference, if user logined saved, call navigation pane with user name header
        LoadNavigationHeader loadNavigationHeader = new LoadNavigationHeader(NewsApiPage.this, navigationView);
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
        drawerNewsAPI = findViewById(R.id.newsapi_page);
        toolbar = findViewById(R.id.nav_button);
        newsTypeView = findViewById(R.id.news_type);
        newsViewHorizontal = findViewById(R.id.cardnews_view_horizontal);
        newsViewVertical = findViewById(R.id.cardnews_view_vertical);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        //create navigation drawer
        navigationPane = new NavigationPane(drawerNewsAPI, this, toolbar, navigationView, R.id.newsapi_menu);
        navigationPane.CallFromUser();
        //From SharedPreference, load country code
        reloadCountryCode();
        //open sign in page from navigation view
        if (Objects.equals(getUserLogin.getStatus(), "")) {
            CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
            callSignInForm.callSignInForm();
        }
        CheckNetworkConnection checkNetworkConnection = new CheckNetworkConnection();
        if (checkNetworkConnection.checkConnection(this)) {
            //NewsCategory Type List
            loadCategoryType(getCountryCodeDefault());
            loadNewsApi(getCountryCodeDefault());
        }

        //open Country Filter
        openCountryFilter();
        //Hide float button when scroll recyclerview vertical
        hideWhenScroll();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadNewsApi(getCountryCodeDefault());
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCategoryType(String countryCodeDefault) {
        NewsAPITypeAdapter newsAPITypeAdapter = new NewsAPITypeAdapter(this, countryCodeDefault);
        LinearLayoutManager newstypeLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        newsTypeView.setLayoutManager(newstypeLayout);
        newsTypeView.setAdapter(newsAPITypeAdapter);
        newsAPITypeAdapter.notifyDataSetChanged();
    }

    private void hideWhenScroll() {
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (nestedScrollView.getScrollY() == 0) {
                filterCountry.extend();
            } else {
                filterCountry.shrink();
            }
        });
    }

    private void loadNewsApi(String countryCodeDefault) {
        //Loading Message
        MaterialAltertLoading materialAltertLoading = new MaterialAltertLoading(this);
        MaterialAlertDialogBuilder mDialog = materialAltertLoading.getDialog();
        AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
        //Load JSONData and apply to RecycleView Horizontal Latest NewsCategory
        loadJsonLatestNews(this, alertDialog,"", countryCodeDefault, "");
        //Load JSONData Business NewsCategory and apply to RecycleView Vertical Latest NewsCategory
        newsAPICategory.LoadJSONCategory(this, alertDialog, "", newsViewVertical, countryCodeDefault, "");
    }

    private void openCountryFilter() {
        filterCountry.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(NewsApiPage.this);
            bottomSheetDialog.setContentView(R.layout.choose_feed_newsapi);
            bottomSheetDialog.show();
            MaterialAutoCompleteTextView spinner_rss;
            spinner_rss = bottomSheetDialog.findViewById(R.id.spinner_rss);
            chooseTitle = bottomSheetDialog.findViewById(R.id.choose_title);
            chooseHint = bottomSheetDialog.findViewById(R.id.hint_to_choose);
            chooseTitle.setText(R.string.choose_country_newsapi);
            chooseHint.setHint(R.string.choose_country_hint);
            call = newsAppInterface.getNewsApiCountrySupport();
            countryList = new ArrayList<>();
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<NewsAppResult> call, @NonNull Response<NewsAppResult> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getCountryList() != null) {
                            if (!countryList.isEmpty()) {
                                countryList.clear();
                            }
                            countryList = response.body().getCountryList();
                            List<Country> countryListName = countryList;
                            ArrayList<String> countryName = new ArrayList<>();
                            for (Country country : countryListName) {
                                countryName.add(country.getCountryName());
                            }
                            assert spinner_rss != null;
                            spinner_rss.setAdapter(new ArrayAdapter<>(NewsApiPage.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, countryName));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                    Logger.getLogger("Error").warning(t.getMessage());
                }
            });
            Button okBtn = bottomSheetDialog.findViewById(R.id.btnLoad);
            assert okBtn != null;
            assert spinner_rss != null;
            okBtn.setOnClickListener(v1 -> {
                String countryName = spinner_rss.getText().toString();
                if (countryName.isEmpty()) {
                    Toast.makeText(NewsApiPage.this, R.string.country_not_choose, Toast.LENGTH_SHORT).show();
                } else {
                    loadCountryCode(countryName);
                    bottomSheetDialog.dismiss();
                }

            });
        });
    }

    private void loadCountryCode(String countryName) {
        call = newsAppInterface.getNewsApiCountryCode(Utils.encodeToBase64(countryName));
        assert call != null;
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<NewsAppResult> call, @NonNull Response<NewsAppResult> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String code = response.body().getCountryCode();
                    //Save shared preference
                    SharedPreferenceSettings sharedPreferenceSettings = new SharedPreferenceSettings(NewsApiPage.this);
                    assert code != null;
                    sharedPreferenceSettings.getSharedCountry(code);
                    setCountryCodeDefault(code);
                    loadCategoryType(code);
                    //load newsapi again
                    loadNewsApi(code);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                Logger.getLogger("Error").warning(t.getMessage());
            }
        });
    }

    public void loadJsonLatestNews(AppCompatActivity activity, AlertDialog mDialog,String keyWord, String country, String category) {
        Thread loadSourceAPI = new Thread(() -> {
            call = newsAppInterface.getNewsTopHeadlinesFromNewsApi(Utils.encodeToBase64(keyWord), country, category);
            assert call != null;
            call.enqueue(new Callback<>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<NewsAppResult> call, @NonNull Response<NewsAppResult> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getArticle() != null) {
                            if (!articles.isEmpty()) {
                                articles.clear();
                            }
                            articles = response.body().getArticle();
                            newsAPIHorizontalLayout = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                            newsViewHorizontal.setLayoutManager(newsAPIHorizontalLayout);
                            newsAdapterHorizontal = new NewsAdapterHorizontal(articles, activity);
                            newsViewHorizontal.setAdapter(newsAdapterHorizontal);
                            newsAdapterHorizontal.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                    Toast.makeText(NewsApiPage.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                }
            });

            runOnUiThread(() ->
                    //close material dialog
                    newsViewHorizontal.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss));
        });
        loadSourceAPI.start();
    }

    private void reloadCountryCode() {
        SharedPreferences sharedPreferences = getSharedPreferences("CountryCode", MODE_PRIVATE);
        if (!sharedPreferences.getString("code", "").equals(getCountryCodeDefault())) {
            setCountryCodeDefault(sharedPreferences.getString("code", getCountryCodeDefault()));
        }
    }
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (drawerNewsAPI.isDrawerOpen(GravityCompat.START)) {
                drawerNewsAPI.closeDrawer(GravityCompat.START);
            } else {
                ActivityOptions.makeSceneTransitionAnimation(NewsApiPage.this).toBundle();
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
            case R.id.home_menu -> {
                intent = new Intent(NewsApiPage.this, HomePage.class);
                startActivity(intent);
                this.finish();
            }
            case R.id.source_menu -> {
                intent = new Intent(NewsApiPage.this, SourceNewsList.class);
                startActivity(intent);
                this.finish();
            }
            case R.id.favourite_menu -> {
                intent = new Intent(NewsApiPage.this, FavouriteNews.class);
                startActivity(intent);
                this.finish();
            }
            case R.id.settings_menu -> {
                OpenSettingsPage openSettingsPage = new OpenSettingsPage(NewsApiPage.this);
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
        navigationPane = new NavigationPane(drawerNewsAPI, this, toolbar, navigationView, R.id.newsapi_menu);
        navigationPane.CallFromUser();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}