package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.content.Context
import android.content.SharedPreferences

class UseChromeShared(var context: Context) {
    var pref: SharedPreferences = context.getSharedPreferences(USE_CHROME, Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = pref.edit()

    var enableChrome: Boolean
        get() = pref.getBoolean(USE_CHROME_DEFAULT, false)
        set(value) {
            editor.putBoolean(USE_CHROME_DEFAULT, value)
            editor.commit()
        }

    companion object {
        const val USE_CHROME = "useChrome"
        const val USE_CHROME_DEFAULT = "useChromeDefault"
    }
}