package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.content.Context
import android.content.SharedPreferences

class SaveThemeSettings(var _context: Context) {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor

    init {
        pref = _context.getSharedPreferences("Theme", Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    fun saveTheme(theme: String?) {
        editor.putString("theme", theme)
        editor.apply()
    }
}