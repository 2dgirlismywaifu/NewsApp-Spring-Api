/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notmiyouji.newsapp.java.general;

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (activity, drawerLayout, toolbar, R.string.navgiation_drawer_open, R.string.navgiation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(menuText);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) activity);
    }
}
