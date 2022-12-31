package com.notmiyouji.newsapp.java.global;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;


import com.google.android.material.navigation.NavigationView;
import com.notmiyouji.newsapp.R;

public class LoadWallpaperShared {
    //This class will load the wallpaper from shared preferences for header navigation
    NavigationView navigationView;
    AppCompatActivity activity;
    SharedPreferences prefs;

    View headerNavigation;
    LinearLayout header_guest;

    public LoadWallpaperShared(NavigationView navigationView, AppCompatActivity activity) {
        this.navigationView = navigationView;
        this.activity = activity;
        headerNavigation = navigationView.getHeaderView(0);
        header_guest = (LinearLayout) headerNavigation.findViewById(R.id.header_guest);
    }
    private int getBackground() {
        prefs = activity.getSharedPreferences("Wallpaper", MODE_PRIVATE);
        return prefs.getInt("path",header_guest.getBackground().getCurrent().getConstantState().getChangingConfigurations());
    }
    public void loadWallpaper() {
        if (getBackground() != header_guest.getBackground().getCurrent().getConstantState().getChangingConfigurations()) {
            header_guest.setBackground(ResourcesCompat.getDrawable(activity.getResources(), getBackground(), null));
        }
    }
}
