package com.notmiyouji.newsapp.java.NewsAPI;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.RSSURL.FavouriteNews;
import com.notmiyouji.newsapp.java.RSSURL.HomePage;
import com.notmiyouji.newsapp.java.RSSURL.SourceNewsList;
import com.notmiyouji.newsapp.java.global.NavigationPane;
import com.notmiyouji.newsapp.java.global.SettingsPage;
import com.notmiyouji.newsapp.java.global.recycleviewadapter.NewsAPITypeAdapter;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.CallSignInForm;
import com.notmiyouji.newsapp.kotlin.NewsAPIInterface;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Article;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.News;
import com.notmiyouji.newsapp.kotlin.sharedSettings.LoadWallpaperShared;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAPI_Page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    //Initialization variable
    public static final String API_KEY = new NewsAPIKey().getNEWSAPIKEY(); //the newsAPI key is here
    DrawerLayout drawerNewsAPI;
    NavigationView navigationView;
    LinearLayoutManager newstypeLayout;
    LinearLayoutManager newsAPIHorizontalLayout;
    RecyclerView newstypeView, newsViewHorizontal, newsViewVertical;
    Toolbar toolbar;
    NavigationPane navigationPane;
    NewsAPITypeAdapter newsAPITypeAdapter;
    Intent intent;
    List<Article> articles = new ArrayList<>(); //not include category
    NewsAdapterHorizontal newsAdapterHorizontal;
    NewsAPIInterface newsApiInterface = NewsAPIKey.getAPIClient().create(NewsAPIInterface.class);
    Call<News> call;
    LoadFollowCategory loadFollowCategory = new LoadFollowCategory();
    LoadWallpaperShared loadWallpaperShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_api_page);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        drawerNewsAPI = findViewById(R.id.newsapi_page);
        navigationView = findViewById(R.id.nav_pane_view);
        toolbar = findViewById(R.id.nav_button);
        newstypeView = findViewById(R.id.news_type);
        newsViewHorizontal = findViewById(R.id.cardnews_view_horizontal);
        newsViewVertical = findViewById(R.id.cardnews_view_vertical);
        //create navigation drawer
        navigationPane = new NavigationPane(drawerNewsAPI, this, toolbar, navigationView, R.id.newsapi_menu);
        navigationPane.CallFromUser();
        //From SharedPreference, change background for header navigation pane
        loadWallpaperShared = new LoadWallpaperShared(navigationView, this);
        loadWallpaperShared.loadWallpaper();
        //open sign in page from navigationview
        CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
        callSignInForm.callSignInForm();
        //NewsCategory Type List
        newsAPITypeAdapter = new NewsAPITypeAdapter(this);
        newstypeLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        newstypeView.setLayoutManager(newstypeLayout);
        newstypeView.setAdapter(newsAPITypeAdapter);
        //Loading Messeage
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading, please wait...");
        mDialog.show();
        //Load JSONData and apply to RecycleView Horizontal Lastest NewsCategory
        LoadJSONLastestNews(this, mDialog);
        //Load JSONData Business NewsCategory and apply to RecycleView Vertical Lastest NewsCategory
        loadFollowCategory.LoadJSONCategory(this, mDialog, "business", newsViewVertical);
    }

    public void LoadJSONLastestNews(AppCompatActivity activity, ProgressDialog mDialog){
        Thread loadSourceAPI = new Thread(() -> {
            call = newsApiInterface.getLatestNews("us", API_KEY);
            assert call != null;
            call.enqueue(new Callback<News>() {
                @Override
                public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
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
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                    Toast.makeText(NewsAPI_Page.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                }
            });

            runOnUiThread(() ->
                    newsViewHorizontal.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss));
        });
        loadSourceAPI.start();
    }


    @Override
    public void onBackPressed() {
        if (drawerNewsAPI.isDrawerOpen(GravityCompat.START)) {
            drawerNewsAPI.closeDrawer(GravityCompat.START);
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
            intent = new Intent(NewsAPI_Page.this, HomePage.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.source_menu) {
            intent = new Intent(NewsAPI_Page.this, SourceNewsList.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.favourite_menu) {
            intent = new Intent(NewsAPI_Page.this, FavouriteNews.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.settings_menu) {
            intent = new Intent(NewsAPI_Page.this, SettingsPage.class);
            startActivity(intent);
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        loadWallpaperShared.loadWallpaper();
    }
}