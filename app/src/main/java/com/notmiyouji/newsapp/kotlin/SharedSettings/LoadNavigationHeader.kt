package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.notmiyouji.newsapp.R

class LoadNavigationHeader(var activity: AppCompatActivity, var navigationView: NavigationView) {
    fun loadHeader() {
        val sharedPreferences = activity.getSharedPreferences("userLogined", Context.MODE_PRIVATE)
        //Get String from sharedPreference
        val username = sharedPreferences.getString("username", "")
        val password = sharedPreferences.getString("password", "")
        val email = sharedPreferences.getString("email", "")
        if (username != "" && password != "" && email != "") {
            navigationView.inflateHeaderView(R.layout.navigation_header_logined)
            val fullnameHeader =
                navigationView.getHeaderView(0).findViewById<TextView>(R.id.fullname)
            val usernameHeader =
                navigationView.getHeaderView(0).findViewById<TextView>(R.id.user_name)
        } else {
            navigationView.inflateHeaderView(R.layout.navigation_header)
        }
    }
}