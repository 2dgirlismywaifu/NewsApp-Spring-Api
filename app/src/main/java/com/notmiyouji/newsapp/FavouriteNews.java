package com.notmiyouji.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.notmiyouji.newsapp.global.NavigationPane;

public class FavouriteNews extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Initialization variable
    DrawerLayout drawerFavourtie;
    NavigationView navigationView;
    Toolbar toolbar;
    NavigationPane navigationPane;
    Intent intent;
    Thread favouritepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_news);

        //Hooks
        drawerFavourtie = findViewById(R.id.favourite_news_page);
        navigationView = findViewById(R.id.nav_pane_view);
        toolbar = findViewById(R.id.nav_button);
        navigationPane = new NavigationPane(drawerFavourtie, this, toolbar, navigationView, R.id.favourite_menu);
        navigationPane.CallFromUser();
    }

    @Override
    public void onBackPressed() {
        if (drawerFavourtie.isDrawerOpen(GravityCompat.START)) {
            drawerFavourtie.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        favouritepage = new Thread() {
            @Override
            public void run() {
                int menuitem = item.getItemId();
                if (menuitem == R.id.home_menu) {
                    intent = new Intent(FavouriteNews.this, HomePage.class);
                    startActivity(intent);
                }
                else if (menuitem == R.id.source_menu) {
                    intent = new Intent(FavouriteNews.this, SourceNewsList.class);
                    startActivity(intent);
                }
                else if (menuitem == R.id.newsapi_menu) {
                    intent = new Intent(FavouriteNews.this, NewsAPI_Page.class);
                    startActivity(intent);
                }
            }
        };
        favouritepage.start();

        return true;
    }
}