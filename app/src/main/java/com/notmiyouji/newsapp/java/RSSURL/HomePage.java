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

import com.google.android.material.navigation.NavigationView;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.NewsAPI.NewsAPI_Page;
import com.notmiyouji.newsapp.kotlin.CallSignInForm;
import com.notmiyouji.newsapp.java.global.NavigationPane;
import com.notmiyouji.newsapp.java.global.recycleviewadapter.NewsTypeAdapter;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Initialization variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayoutManager linearLayoutManager, newsViewLayoutHorizontal;
    RecyclerView recyclerView, newsViewHorizontal, newsViewVertical;
    Toolbar toolbar;
    NavigationPane navigationPane;
    NewsTypeAdapter newsTypeAdapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        drawerLayout = findViewById(R.id.home_page);
        navigationView = findViewById(R.id.nav_pane_view);
        toolbar = findViewById(R.id.nav_button);
        recyclerView = findViewById(R.id.news_type);
        newsViewHorizontal = findViewById(R.id.cardnews_view_horizontal);
        newsViewVertical = findViewById(R.id.cardnews_view_vertical);
        navigationPane = new NavigationPane(drawerLayout, this, toolbar, navigationView, R.id.home_menu);
        navigationPane.CallFromUser();
        //NewsCategory Type List
        newsTypeAdapter = new NewsTypeAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsTypeAdapter);
        //open sign in page from navigationview
        CallSignInForm callSignInForm = new CallSignInForm(navigationView, this);
        callSignInForm.callSignInForm();
        //Load Lastest News
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading, please wait...");
        mDialog.show();
        Thread loadLastNews = new Thread(new Runnable() {
            @Override
            public void run() {
                newsViewLayoutHorizontal = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
                FeedMultiRSS feedMultiRSS = new FeedMultiRSS(HomePage.this, newsViewHorizontal, newsViewLayoutHorizontal);
                feedMultiRSS.MultiFeedHorizontal("Breaking News", mDialog);
            }
        });
        loadLastNews.start();

        //load NewsView Vertical
        LoadFollowType loadFollowType = new LoadFollowType(HomePage.this, newsViewVertical, mDialog);
        loadFollowType.startLoad("Breaking News");
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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
        if (menuitem == R.id.newsapi_menu) {
            intent = new Intent(HomePage.this, NewsAPI_Page.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.source_menu) {
            intent = new Intent(HomePage.this, SourceNewsList.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.favourite_menu) {
            intent = new Intent(HomePage.this, FavouriteNews.class);
            startActivity(intent);
        }
        return true;
    }
}