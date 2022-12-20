package com.notmiyouji.newsapp.java.global;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.notmiyouji.newsapp.R;

public class NavigationPane {

    DrawerLayout drawerLayout;
    AppCompatActivity activity;
    Toolbar toolbar;
    NavigationView navigationView;
    int menuText;

    public NavigationPane(DrawerLayout drawerLayout, AppCompatActivity activity, Toolbar toolbar,
                          NavigationView navigationView, int menuText) {
        this.drawerLayout = drawerLayout;
        this.activity = activity;
        this.toolbar = toolbar;
        this.navigationView = navigationView;
        this.menuText = menuText;
    }

    public void CallFromUser() {
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle
                (activity, drawerLayout, toolbar, R.string.navgiation_drawer_open, R.string.navgiation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        navigationView.setCheckedItem(menuText);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) activity);
    }
}
