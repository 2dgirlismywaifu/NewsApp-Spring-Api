package com.notmiyouji.newsapp.java.RSSURL;

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
import com.notmiyouji.newsapp.java.global.NavigationPane;
import com.notmiyouji.newsapp.java.global.recycleviewadapter.ListSourceAdapter;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_news_list);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        drawerSourceNews = findViewById(R.id.source_news_page);
        navigationView = findViewById(R.id.nav_pane_view);
        toolbar = findViewById(R.id.nav_button);
        navigationPane = new NavigationPane(drawerSourceNews, this, toolbar, navigationView, R.id.source_menu);
        navigationPane.CallFromUser();
        //Recycle View
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading, please wait...");
        mDialog.show();
        loadSourceList(this, mDialog);
    }

    public void loadSourceList(AppCompatActivity activity, ProgressDialog mDialog) {
        Thread loadSource = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    recyclerView = findViewById(R.id.sources_list);
                    linearLayoutManager = new LinearLayoutManager(getBaseContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    listSourceAdapter = new ListSourceAdapter(activity);
                    recyclerView.setAdapter(listSourceAdapter);
                    recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss);
                });
            }
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
            intent = new Intent(SourceNewsList.this, NewsAPI_Page.class);
            startActivity(intent);
        }
        else if (menuitem == R.id.favourite_menu) {
            intent = new Intent(SourceNewsList.this, FavouriteNews.class);
            startActivity(intent);
        }

        return true;
    }
}