package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import com.notmiyouji.newsapp.java.SharedSettings.LanguagePrefManager

class LoadFollowLanguageSystem(private val activity: AppCompatActivity) {
    var languagePrefManager: LanguagePrefManager? = null
    fun loadLanguage() {
        languagePrefManager = LanguagePrefManager(activity)
        if (languagePrefManager!!.lang != "system") {
            languagePrefManager!!.setLocal(languagePrefManager!!.lang)
        } else {
            val default_local = Resources.getSystem().configuration.locales[0].language
            languagePrefManager!!.setLocal(default_local)
        }
        languagePrefManager!!.loadLocal()
    }
}