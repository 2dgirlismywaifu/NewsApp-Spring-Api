package com.notmiyouji.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.notmiyouji.newsapp.global.NavigationPane;

public class SourceNewsList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Initialization variable
    DrawerLayout drawerSourceNews;
    NavigationView navigationView;
    Toolbar toolbar;
    NavigationPane navigationPane;
    Intent intent;
    Thread sourcepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_news_list);

        //Hooks
        drawerSourceNews = findViewById(R.id.source_news_page);
        navigationView = findViewById(R.id.nav_pane_view);
        toolbar = findViewById(R.id.nav_button);
        navigationPane = new NavigationPane(drawerSourceNews, this, toolbar, navigationView, R.id.source_menu);
        navigationPane.CallFromUser();
    }

    @Override
    public void onBackPressed() {
        if (drawerSourceNews.isDrawerOpen(GravityCompat.START)) {
            drawerSourceNews.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        sourcepage = new Thread() {
            @Override
            public void run() {
                int menuitem = item.getItemId();
                if (menuitem == R.id.home_menu) {
                    intent = new Intent(SourceNewsList.this, HomePage.class);
                    startActivity(intent);
                }
                else if (menuitem == R.id.newsapi_menu) {
                    intent = new Intent(SourceNewsList.this, NewsAPI_Page.class);
                    startActivity(intent);
                }
                else if (menuitem == R.id.favourite_menu) {
                    intent = new Intent(SourceNewsList.this, FavouriteNews.class);
                    startActivity(intent);
                }
            }
        };
        sourcepage.start();

        return true;
    }
}