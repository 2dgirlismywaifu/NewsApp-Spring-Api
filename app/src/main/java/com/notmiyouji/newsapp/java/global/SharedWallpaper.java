package com.notmiyouji.newsapp.java.global;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class SharedWallpaper {
    SharedPreferences prefs;
    Context context;
    public SharedWallpaper(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("Wallpaper", Context.MODE_PRIVATE);
    }

    public void getSharedWallpaperHeader(int path) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("path", path);
        editor.apply();
    }
}
