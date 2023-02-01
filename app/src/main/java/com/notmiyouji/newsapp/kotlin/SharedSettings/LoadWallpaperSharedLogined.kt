package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.navigation.NavigationView
import com.notmiyouji.newsapp.R

class LoadWallpaperSharedLogined(//This class will load the wallpaper from shared preferences for header navigation
    navigationLogined: NavigationView, var activity: AppCompatActivity
) {
    private var headerLogined: View = navigationLogined.getHeaderView(0)
    private var header_logined: LinearLayout =
        headerLogined.findViewById<View>(R.id.header_login) as LinearLayout

    private fun requestBackground(): Int {
        val prefs = activity.getSharedPreferences("Wallpaper", Context.MODE_PRIVATE)
        val backgroundcode = prefs.getInt(
            "path", header_logined.background.current.constantState!!.changingConfigurations
        )
        return backgroundcode
    }

    fun loadWallpaper() {
        if (requestBackground() != header_logined.background.current.constantState!!.changingConfigurations) {
            header_logined.background =
                ResourcesCompat.getDrawable(activity.resources, requestBackground(), null)
        }
    }
}