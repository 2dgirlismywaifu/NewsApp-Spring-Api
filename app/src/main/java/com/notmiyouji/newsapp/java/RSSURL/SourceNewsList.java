package com.notmiyouji.newsapp.java.RSSURL;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.NewsAPI.NewsAPIPage;
import com.notmiyouji.newsapp.java.global.LanguagePrefManager;
import com.notmiyouji.newsapp.java.global.NavigationPane;
import com.notmiyouji.newsapp.java.global.SettingsPage;
import com.notmiyouji.newsapp.java.global.recycleviewadapter.ListSourceAdapter;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject;
import com.notmiyouji.newsapp.kotlin.RSSSource.NewsSource;
import com.notmiyouji.newsapp.kotlin.sharedSettings.LoadWallpaperShared;

import java.util.ArrayList;
import java.util.List;
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
    SwipeRefreshLayout swipeRefreshLayout;
    NewsAPPInterface newsAPPInterface = NewsAppAPI.getAPIClient().create(NewsAPPInterface.class);
    List<NewsSource> newsSources = new ArrayList<>();
    LanguagePrefManager languagePrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        languagePrefManager = new LanguagePrefManager(getBaseContext());
        languagePrefManager.setLocal(languagePrefManager.getLang());
        languagePrefManager.loadLocal();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_news_list);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        drawerSourceNews = findViewById(R.id.source_news_page);
        navigationView = findViewById(R.id.nav_pane_view);
        toolbar = findViewById(R.id.nav_button);
        recyclerView = findViewById(R.id.sources_list);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        navigationPane = new NavigationPane(drawerSourceNews, this, toolbar, navigationView, R.id.source_menu);
        navigationPane.CallFromUser();
        //From SharedPreference, change background for header navigation pane
        loadWallpaperShared = new LoadWallpaperShared(navigationView, this);
        loadWallpaperShared.loadWallpaper();
        //open sign in page from navigationview
        CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
        callSignInForm.callSignInForm();
        //Recycle View
        loadSourceList(this);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadSourceList(SourceNewsList.this);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    public void loadSourceList(AppCompatActivity activity) {
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading, please wait...");
        mDialog.show();
        Thread loadSource = new Thread(() -> {
            Call<ListObject> call = newsAPPInterface.getAllSource();
            assert call != null;
            call.enqueue(new retrofit2.Callback<ListObject>() {
                @Override
                public void onResponse(@NonNull Call<ListObject> call, @NonNull retrofit2.Response<ListObject> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getNewsSource() !=null) {
                            if (!newsSources.isEmpty()) {
                                newsSources.clear();
                            }
                            newsSources = response.body().getNewsSource();
                            linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            listSourceAdapter = new ListSourceAdapter(activity, newsSources);
                            recyclerView.setAdapter(listSourceAdapter);
                            runOnUiThread(() -> recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss));
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
        }
        else {
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
        }
        else if (menuitem == R.id.newsapi_menu) {
            intent = new Intent(SourceNewsList.this, NewsAPIPage.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.favourite_menu) {
            intent = new Intent(SourceNewsList.this, FavouriteNews.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.settings_menu) {
            intent = new Intent(SourceNewsList.this, SettingsPage.class);
            startActivity(intent);
        }

        return true;
    }
    public void onResume() {
        super.onResume();
        loadWallpaperShared.loadWallpaper();
        languagePrefManager.loadLocal();
    }
}