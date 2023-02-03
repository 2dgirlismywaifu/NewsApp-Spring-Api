package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.content.Context
import android.content.SharedPreferences

class SaveUserLogined(context: Context) {
    var sharedPreferences: SharedPreferences
    var editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences("UserLogined", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun saveUserLogined(
        userID: String?,
        fullname: String?,
        email: String?,
        password: String?,
        username: String?,
        avatar: String?,
        status: String?
    ) {
        editor.putString("userID", userID)
        editor.putString("fullname", fullname)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putString("username", username)
        editor.putString("avatar", avatar)
        editor.putString("status", status)
        editor.apply()
    }
}