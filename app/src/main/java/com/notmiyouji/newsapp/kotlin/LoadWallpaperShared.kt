package com.notmiyouji.newsapp.kotlin

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.navigation.NavigationView
import com.notmiyouji.newsapp.R

class LoadWallpaperShared(//This class will load the wallpaper from shared preferences for header navigation
    navigationView: NavigationView, var activity: AppCompatActivity
) {
    private var headerNavigation: View = navigationView.getHeaderView(0)
    private var header_guest: LinearLayout = headerNavigation.findViewById<View>(R.id.header_guest) as LinearLayout

    private fun requestBackground(): Int {
        val prefs = activity.getSharedPreferences("Wallpaper", Context.MODE_PRIVATE)
        val backgroundcode = prefs.getInt(
            "path", header_guest.background.current.constantState!!.changingConfigurations
        )
        return backgroundcode
    }

    fun loadWallpaper() {
        if (requestBackground() != header_guest.background.current.constantState!!.changingConfigurations) {
            header_guest.background =
                ResourcesCompat.getDrawable(activity.resources, requestBackground(), null)
        }
    }
}