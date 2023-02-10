package com.notmiyouji.newsapp.java.Global;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.RecycleViewAdapter.WallpaperHeaderAdpater;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;

public class WallpaperHeader extends AppCompatActivity {
    WallpaperHeaderAdpater wallpaperHeaderAdpater;
    RecyclerView recyclerView;
    LoadFollowLanguageSystem loadFollowLanguageSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_header);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        recyclerView = findViewById(R.id.wallpaperRecyclerView);
        //Put Picture to RecycleView
        //Need use thread to load 20 pictures
        LoadWallpaperList(this);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
    }

    private void LoadWallpaperList(AppCompatActivity activity) {
        Thread loadWallpaer = new Thread(() -> {
            wallpaperHeaderAdpater = new WallpaperHeaderAdpater(activity);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            runOnUiThread(() -> recyclerView.setAdapter(wallpaperHeaderAdpater));
        });
        loadWallpaer.start();
    }

    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }

    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
    }
}