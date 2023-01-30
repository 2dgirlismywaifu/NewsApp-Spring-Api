package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class GetUserLogined(var activity: AppCompatActivity) {
    var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = activity.getSharedPreferences("userLogined", Context.MODE_PRIVATE)
    }

    val email: String?
        get() = sharedPreferences.getString("email", "")
    val password: String?
        get() = sharedPreferences.getString("password", "")
    val username: String?
        get() = sharedPreferences.getString("username", "")
}