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

    fun saveUserLogined(email: String?, password: String?, username: String?, status: String?) {
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putString("username", username)
        editor.putString("status", status)
        editor.apply()
    }
}