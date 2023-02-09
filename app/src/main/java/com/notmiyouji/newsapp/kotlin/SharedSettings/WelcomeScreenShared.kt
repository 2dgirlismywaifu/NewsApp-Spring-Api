package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.content.Context
import android.content.SharedPreferences

class WelcomeScreenShared(var context: Context) {
    var pref: SharedPreferences = context.getSharedPreferences(WELCOME_SCREEN, Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = pref.edit()

    var enableWelcome: Boolean
        get() = pref.getBoolean(SHOW_WELCOME, false)
        set(value) {
            editor.putBoolean(SHOW_WELCOME, value)
            editor.commit()
        }

    companion object {
        const val WELCOME_SCREEN = "welcomeScreen"
        const val SHOW_WELCOME = "showWelcomeScreen"
    }
}