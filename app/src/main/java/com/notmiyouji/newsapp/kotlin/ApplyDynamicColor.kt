package com.notmiyouji.newsapp.kotlin

import android.app.Application
import com.google.android.material.color.DynamicColors

class ApplyDynamicColor : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}