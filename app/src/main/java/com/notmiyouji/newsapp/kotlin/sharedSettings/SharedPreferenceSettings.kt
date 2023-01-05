package com.notmiyouji.newsapp.kotlin.sharedSettings

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceSettings(var context: Context) {
    var prefs: SharedPreferences = context.getSharedPreferences("Wallpaper", Context.MODE_PRIVATE)
    var sourceprefs: SharedPreferences = context.getSharedPreferences("SourceName", Context.MODE_PRIVATE)

    fun getSharedWallpaperHeader(path: Int) {
        val editor = prefs.edit()
        editor.putInt("path", path)
        editor.apply()
    }

    fun getSharedSource(name: String) {
        val editor = sourceprefs.edit()
        editor.putString("name", name)
        editor.apply()
    }
}