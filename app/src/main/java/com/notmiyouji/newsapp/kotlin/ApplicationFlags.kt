package com.notmiyouji.newsapp.kotlin

import android.annotation.SuppressLint
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class ApplicationFlags(var activity: AppCompatActivity) {
    @SuppressLint("SourceLockedOrientationActivity")
    fun setFlag() {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        //lock screen orientation to only portrait (not recommended to do this)
        activity.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}