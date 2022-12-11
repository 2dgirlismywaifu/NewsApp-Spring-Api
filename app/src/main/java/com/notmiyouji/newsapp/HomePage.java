package com.notmiyouji.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.navigation.NavigationView;
import com.notmiyouji.newsapp.global.NavigationPane;
import com.notmiyouji.newsapp.global.NewsTypeAdapter;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Initialization variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    Toolbar toolbar;
    NavigationPane navigationPane;
    NewsTypeAdapter newsTypeAdapter;
    Intent intent;
    Thread homepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Hooks
        drawerLayout = findViewById(R.id.home_page);
        navigationView = findViewById(R.id.nav_pane_view);
        toolbar = findViewById(R.id.nav_button);
        recyclerView = findViewById(R.id.news_type);
        navigationPane = new NavigationPane(drawerLayout, this, toolbar, navigationView, R.id.home_menu);
        navigationPane.CallFromUser();
        //News Type List
        newsTypeAdapter = new NewsTypeAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsTypeAdapter);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        homepage = new Thread(){
            @Override
            public void run() {
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
            }
        };
        homepage.start();


        return true;
    }
}