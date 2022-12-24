package com.notmiyouji.newsapp.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager

class ApplicationFlags(var activity: AppCompatActivity) {
    fun setFlag() {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}