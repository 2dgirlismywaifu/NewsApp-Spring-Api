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

    fun saveFullname(fullname: String?) {
        editor.putString("fullname", fullname)
        editor.apply()
    }

    fun saveUsername(username: String?) {
        editor.putString("username", username)
        editor.apply()
    }

    fun saveAvatar(avatar: String?) {
        editor.putString("avatar", avatar)
        editor.apply()
    }

    fun saveGender(gender: String?) {
        editor.putString("gender", gender)
        editor.apply()
    }

    fun saveBirthday(birthday: String?) {
        editor.putString("birthday", birthday)
        editor.apply()
    }
}