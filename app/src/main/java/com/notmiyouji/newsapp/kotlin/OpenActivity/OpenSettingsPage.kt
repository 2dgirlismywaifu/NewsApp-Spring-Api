package com.notmiyouji.newsapp.kotlin.OpenActivity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.notmiyouji.newsapp.java.Global.SettingsPage
import com.notmiyouji.newsapp.java.Signed.SettingsLogined
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined

class OpenSettingsPage(var activity: AppCompatActivity) {
    var sharedPreferences: SharedPreferences? = null
    var intent: Intent? = null
    fun openSettings() {
        val getUserLogined = GetUserLogined(activity)
        if (getUserLogined.status == "login" || getUserLogined.status == "google") {
            intent = Intent(activity, SettingsLogined::class.java)
            activity.startActivity(intent)
        } else {
            intent = Intent(activity, SettingsPage::class.java)
            activity.startActivity(intent)
        }
    }
}