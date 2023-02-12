package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.kotlin.LoadImageURL

class LoadNavigationHeader(var activity: AppCompatActivity, private var navigationView: NavigationView) {
    @SuppressLint("SetTextI18n")
    fun loadHeader() {
        val sharedPreferences = activity.getSharedPreferences("UserLogined", Context.MODE_PRIVATE)
        //Get String from sharedPreference
        val fullname = sharedPreferences.getString("fullname", "")
        val username = sharedPreferences.getString("username", "")
        val avatar = sharedPreferences.getString("avatar", "")
        val status = sharedPreferences.getString("status", "")
        when {
            status.equals("login") || status.equals("google") -> {
                navigationView.inflateHeaderView(R.layout.navigation_header_logined)
                val fullnameHeader =
                    navigationView.getHeaderView(0).findViewById<TextView>(R.id.fullname)
                fullnameHeader.text = fullname
                val usernameHeader =
                    navigationView.getHeaderView(0).findViewById<TextView>(R.id.user_name)
                usernameHeader.text = "@$username"
                val avatarHeader =
                    navigationView.getHeaderView(0).findViewById<ShapeableImageView>(R.id.avatar_user)
                //Call LoadImageURL.kt
                val loadImageURL = LoadImageURL(avatar)
                loadImageURL.loadImageUser(avatarHeader)
            }
            else -> {
                //load default header

                navigationView.inflateHeaderView(R.layout.navigation_header)
            }
        }
    }
}