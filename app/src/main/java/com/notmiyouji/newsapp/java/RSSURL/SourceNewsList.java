package com.notmiyouji.newsapp.java.RSSURL;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import com.notmiyouji.newsapp.java.Global.FavouriteNews;
import com.notmiyouji.newsapp.java.Global.MaterialAltertLoading;
import com.notmiyouji.newsapp.java.Global.NavigationPane;
import com.notmiyouji.newsapp.java.NewsAPI.NewsAPIPage;
import com.notmiyouji.newsapp.java.RecycleViewAdapter.ListSourceAdapter;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.NetworkConnection;
import com.notmiyouji.newsapp.kotlin.OpenActivity.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.OpenActivity.OpenSettingsPage;
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject;
import com.notmiyouji.newsapp.kotlin.RSSSource.NewsSource;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadNavigationHeader;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadThemeShared;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadWallpaperShared;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadWallpaperSharedLogined;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import retrofit2.Call;

public class SourceNewsList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Initialization variable
    DrawerLayout drawerSourceNews;
    NavigationView navigationView;
    Toolbar toolbar;
    NavigationPane navigationPane;
    Intent intent;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ListSourceAdapter listSourceAdapter;
    LoadWallpaperShared loadWallpaperShared;
    LoadWallpaperSharedLogined loadWallpaperSharedLogined;
    SwipeRefreshLayout swipeRefreshLayout;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);
    List<NewsSource> newsSources = new ArrayList<>();
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    LoadThemeShared loadThemeShared;
    LoadNavigationHeader loadNavigationHeader;
    GetUserLogined getUserLogined;
    EditText searchSource;
    LinearLayout sourceListPage, errorPage;

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
                //Recycle View
                loadSourceList(this);
            } else {
                sourceListPage.setVisibility(LinearLayout.GONE);
                errorPage.setVisibility(LinearLayout.VISIBLE);
            }
        });
        navigationView = findViewById(R.id.nav_pane_sourceList);
        //From sharedPreference, if user logined saved, call navigation pane with user name header
        loadNavigationHeader = new LoadNavigationHeader(this, navigationView);
        loadNavigationHeader.loadHeader();
        //From SharedPreference, change background for header navigation pane
        getUserLogined = new GetUserLogined(this);
        if (getUserLogined.getStatus().equals("login") || getUserLogined.getStatus().equals("google")) {
            loadWallpaperSharedLogined = new LoadWallpaperSharedLogined(navigationView, this);
            loadWallpaperSharedLogined.loadWallpaper();
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
        //open sign in page from navigationview
        if (getUserLogined.getStatus().equals("")) {
            CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
            callSignInForm.callSignInForm();
        }
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadSourceList(SourceNewsList.this);
            swipeRefreshLayout.setRefreshing(false);
        });
        //Recycle View Filter
        searchSource = findViewById(R.id.search_input);
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
                    newsSource.getSource_name()).toLowerCase().contains(s.toLowerCase())) {
                newsSourceList.add(newsSource);
            }
        }
        listSourceAdapter.filterList(newsSourceList);
    }

    public void loadSourceList(AppCompatActivity activity) {
        MaterialAltertLoading materialAltertLoading = new MaterialAltertLoading(this);
        MaterialAlertDialogBuilder mDialog = materialAltertLoading.getDiaglog();
        AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
        Thread loadSource = new Thread(() -> {
            Call<ListObject> call = newsAPPInterface.getAllSource();
            assert call != null;
            call.enqueue(new retrofit2.Callback<ListObject>() {
                @Override
                public void onResponse(@NonNull Call<ListObject> call, @NonNull retrofit2.Response<ListObject> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getNewsSource() != null) {
                            if (!newsSources.isEmpty()) {
                                newsSources.clear();
                            }
                            newsSources = response.body().getNewsSource();
                            linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            listSourceAdapter = new ListSourceAdapter(activity, newsSources);
                            recyclerView.setAdapter(listSourceAdapter);
                            runOnUiThread(() -> recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(alertDialog::dismiss));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ListObject> call, @NonNull Throwable t) {
                    Logger.getLogger("Error").warning(t.getMessage());
                }
            });
        });
        loadSource.start();
    }


    @Override
    public void onBackPressed() {
        if (drawerSourceNews.isDrawerOpen(GravityCompat.START)) {
            drawerSourceNews.closeDrawer(GravityCompat.START);
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
            intent = new Intent(SourceNewsList.this, HomePage.class);
            startActivity(intent);
            this.finish();
        } else if (menuitem == R.id.newsapi_menu) {
            intent = new Intent(SourceNewsList.this, NewsAPIPage.class);
            startActivity(intent);
            this.finish();
        } else if (menuitem == R.id.favourite_menu) {
            intent = new Intent(SourceNewsList.this, FavouriteNews.class);
            startActivity(intent);
            this.finish();
        } else if (menuitem == R.id.settings_menu) {
            OpenSettingsPage openSettingsPage = new OpenSettingsPage(SourceNewsList.this);
            openSettingsPage.openSettings();
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        if (getUserLogined.getStatus().equals("login") || getUserLogined.getStatus().equals("google")) {
            loadWallpaperSharedLogined = new LoadWallpaperSharedLogined(navigationView, this);
            loadWallpaperSharedLogined.loadWallpaper();
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