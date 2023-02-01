package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.notmiyouji.newsapp.R

class LoadNavigationHeader(var activity: AppCompatActivity, var navigationView: NavigationView) {
    @SuppressLint("SetTextI18n")
    fun loadHeader() {
        val sharedPreferences = activity.getSharedPreferences("UserLogined", Context.MODE_PRIVATE)
        //Get String from sharedPreference
        val fullname = sharedPreferences.getString("fullname", "")
        val username = sharedPreferences.getString("username", "")
        val password = sharedPreferences.getString("password", "")
        val email = sharedPreferences.getString("email", "")
        val status = sharedPreferences.getString("status", "")
        if (!status.equals("login")) {
            navigationView.inflateHeaderView(R.layout.navigation_header)
        } else {
            navigationView.inflateHeaderView(R.layout.navigation_header_logined)
            val fullnameHeader =
                navigationView.getHeaderView(0).findViewById<TextView>(R.id.fullname)
            fullnameHeader.text = fullname
            val usernameHeader =
                navigationView.getHeaderView(0).findViewById<TextView>(R.id.user_name)
            usernameHeader.text = "@$username"
        }
    }
}