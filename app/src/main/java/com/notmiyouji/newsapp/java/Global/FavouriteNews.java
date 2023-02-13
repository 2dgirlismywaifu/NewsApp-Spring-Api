package com.notmiyouji.newsapp.java.Global;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.notmiyouji.newsapp.java.NewsAPI.NewsAPIPage;
import com.notmiyouji.newsapp.java.RSSURL.HomePage;
import com.notmiyouji.newsapp.java.RSSURL.SourceNewsList;
import com.notmiyouji.newsapp.java.RecycleViewAdapter.ListSourceAdapter;
import com.notmiyouji.newsapp.java.RecycleViewAdapter.NewsFavouriteAdapter;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.CheckNetworkConnection;
import com.notmiyouji.newsapp.kotlin.FavouriteModel.NewsFavouriteShow;
import com.notmiyouji.newsapp.kotlin.LoginedModel.SignIn;
import com.notmiyouji.newsapp.kotlin.NetworkConnection;
import com.notmiyouji.newsapp.kotlin.OpenActivity.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.OpenActivity.OpenSettingsPage;
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject;
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
    LoadThemeShared loadThemeShared;
    LoadNavigationHeader loadNavigationHeader;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);
    List<NewsFavouriteShow> newsFavourite = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    NewsFavouriteAdapter newsFavouriteAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout errorPage, favouritePage, requestLogin;
    Button signInButton;
    CheckNetworkConnection checkNetworkConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_news);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        signInButton = findViewById(R.id.SignInBtn);
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
        drawerFavourtie = findViewById(R.id.favourite_news_page);
        toolbar = findViewById(R.id.nav_button);
        navigationPane = new NavigationPane(drawerFavourtie, this, toolbar, navigationView, R.id.favourite_menu);
        navigationPane.CallFromUser();
        //open sign in page from navigationview
        if (getUserLogined.getStatus().equals("")) {
            CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
            callSignInForm.callSignInForm();
        }
        checkNetworkConnection = new CheckNetworkConnection();
        if (checkNetworkConnection.CheckConnection(this)) {
            switch (getUserLogined.getStatus()) {
                case "login":
                    loadFavouriteEmail(this);
                    break;
                case "google":
                    loadFavouriteSSO(this);
                    break;
                default:
                    favouritePage.setVisibility(android.view.View.GONE);
                    requestLogin.setVisibility(android.view.View.VISIBLE);
                    Toast.makeText(this, R.string.please_login_to_see_your_favourite_news, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        //Sign in button
        signInButton.setOnClickListener(v -> {
            intent = new Intent(this, SignIn.class);
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent);
        });
        //Swipe to refresh
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            switch (getUserLogined.getStatus()) {
                case "login":
                    loadFavouriteEmail(this);
                    break;
                case "google":
                    loadFavouriteSSO(this);
                    break;
                default:
                    Toast.makeText(this, R.string.please_login_to_see_your_favourite_news, Toast.LENGTH_SHORT).show();
                    break;
            }
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void loadFavouriteEmail(AppCompatActivity activity) {
        MaterialAltertLoading materialAltertLoading = new MaterialAltertLoading(this);
        MaterialAlertDialogBuilder mDialog = materialAltertLoading.getDiaglog();
        AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
        Thread loadSource = new Thread(() -> {
            Call<ListObject> favouriteShowEmail = newsAPPInterface.accountNewsFavouriteShow(getUserLogined.getUserID());
            assert favouriteShowEmail != null;
            favouriteShowEmail.enqueue(new retrofit2.Callback<ListObject>() {
                @Override
                public void onResponse(@NonNull Call<ListObject> call, @NonNull retrofit2.Response<ListObject> response) {
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
                public void onFailure(@NonNull Call<ListObject> call, @NonNull Throwable t) {
                    Logger.getLogger("Error").warning(t.getMessage());
                }
            });

        });
        loadSource.start();
    }

    private void loadFavouriteSSO(AppCompatActivity activity) {
        MaterialAltertLoading materialAltertLoading = new MaterialAltertLoading(this);
        MaterialAlertDialogBuilder mDialog = materialAltertLoading.getDiaglog();
        AlertDialog alertDialog = mDialog.create();
        alertDialog.show();
        Thread loadSSO = new Thread(() -> {
            Call<ListObject> favouriteShowSSO = newsAPPInterface.ssoNewsFavouriteShow(getUserLogined.getUserID());
            assert favouriteShowSSO != null;
            favouriteShowSSO.enqueue(new retrofit2.Callback<ListObject>() {
                @Override
                public void onResponse(@NonNull Call<ListObject> call, @NonNull retrofit2.Response<ListObject> response) {
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
                public void onFailure(@NonNull Call<ListObject> call, @NonNull Throwable t) {
                    Logger.getLogger("Error").warning(t.getMessage());
                }
            });

        });
        loadSSO.start();
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int menuitem = item.getItemId();
        switch (menuitem) {
            case R.id.home_menu:
                intent = new Intent(FavouriteNews.this, HomePage.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.source_menu:
                intent = new Intent(FavouriteNews.this, SourceNewsList.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.newsapi_menu:
                intent = new Intent(FavouriteNews.this, NewsAPIPage.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.settings_menu:
                OpenSettingsPage openSettingsPage = new OpenSettingsPage(FavouriteNews.this);
                openSettingsPage.openSettings();
                break;
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        //From SharedPreference, change background for header navigation pane
        if (getUserLogined.getStatus().equals("login") || getUserLogined.getStatus().equals("google")) {
            loadWallpaperSharedLogined = new LoadWallpaperSharedLogined(navigationView, this);
            loadWallpaperSharedLogined.loadWallpaper();
        } else {
            loadWallpaperShared = new LoadWallpaperShared(navigationView, this);
            loadWallpaperShared.loadWallpaper();
        }
        navigationPane = new NavigationPane(drawerFavourtie, this, toolbar, navigationView, R.id.favourite_menu);
        navigationPane.CallFromUser();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}