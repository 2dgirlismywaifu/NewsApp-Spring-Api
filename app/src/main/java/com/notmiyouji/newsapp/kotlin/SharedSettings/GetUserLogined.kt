package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class GetUserLogined(var activity: AppCompatActivity) {
    var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = activity.getSharedPreferences("UserLogined", Context.MODE_PRIVATE)
    }

    val fullname: String?
        get() = sharedPreferences.getString("fullname", "")
    val email: String?
        get() = sharedPreferences.getString("email", "")
    val password: String?
        get() = sharedPreferences.getString("password", "")
    val username: String?
        get() = sharedPreferences.getString("username", "")
    val avatar: String?
        get() = sharedPreferences.getString("avatar", "")
    val status: String?
        get() = sharedPreferences.getString("status", "")
}