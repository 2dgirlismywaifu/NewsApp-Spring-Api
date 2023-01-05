package com.notmiyouji.newsapp.kotlin

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class ApplicationFlags(var activity: AppCompatActivity) {
    fun setFlag() {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}