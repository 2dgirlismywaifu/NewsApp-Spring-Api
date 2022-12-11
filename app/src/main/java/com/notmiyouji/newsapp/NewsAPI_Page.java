package com.notmiyouji.newsapp;

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
import com.notmiyouji.newsapp.global.NavigationPane;
import com.notmiyouji.newsapp.global.NewsTypeAdapter;

public class NewsAPI_Page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    //Initialization variable
    DrawerLayout drawerNewsAPI;
    NavigationView navigationView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    Toolbar toolbar;
    NavigationPane navigationPane;
    NewsTypeAdapter newsTypeAdapter;
    Intent intent;
    Thread newsapi_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_api_page);
        //Hooks
        drawerNewsAPI = findViewById(R.id.newsapi_page);
        navigationView = findViewById(R.id.nav_pane_view);
        toolbar = findViewById(R.id.nav_button);
        recyclerView = findViewById(R.id.news_type);
        navigationPane = new NavigationPane(drawerNewsAPI, this, toolbar, navigationView, R.id.newsapi_menu);
        navigationPane.CallFromUser();
        //News Type List
        newsTypeAdapter = new NewsTypeAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsTypeAdapter);

    }

    @Override
    public void onBackPressed() {
        if (drawerNewsAPI.isDrawerOpen(GravityCompat.START)) {
            drawerNewsAPI.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        newsapi_page = new Thread() {
            @Override
            public void run() {
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
            }
        };
        newsapi_page.start();

        return true;
    }
}