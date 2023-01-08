package com.notmiyouji.newsapp.kotlin.sharedSettings

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceSettings(var context: Context) {
    var prefs: SharedPreferences = context.getSharedPreferences("Wallpaper", Context.MODE_PRIVATE)
    var sourceprefs: SharedPreferences =
        context.getSharedPreferences("SourceName", Context.MODE_PRIVATE)
    var countryprefs: SharedPreferences =
        context.getSharedPreferences("CountryCode", Context.MODE_PRIVATE)
    var langprefs: SharedPreferences =
        context.getSharedPreferences("LangCode", Context.MODE_PRIVATE)

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

    fun getSharedCountry(code: String) {
        val editor = countryprefs.edit()
        editor.putString("code", code)
        editor.apply()
    }

    fun getSharedLanguage(code: String) {
        val editor = langprefs.edit()
        editor.putString("lang", code)
        editor.apply()
    }
}