package com.notmiyouji.newsapp.kotlin.SharedSettings

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import com.notmiyouji.newsapp.java.SharedSettings.LanguagePrefManager
import java.util.Locale

class LoadFollowLanguageSystem(private val activity: AppCompatActivity) {
    var languagePrefManager: LanguagePrefManager? = null
    fun loadLanguage() {
        languagePrefManager = LanguagePrefManager(activity)
        if (languagePrefManager!!.lang != "system") {
            languagePrefManager!!.setLocal(languagePrefManager!!.lang)
        }
        else if( languagePrefManager!!.lang == "") {
            val default_local = Locale.getDefault().language.toString()
            languagePrefManager!!.setLocal(default_local)
        }
        else {
            val default_local = Locale.getDefault().language.toString()
            languagePrefManager!!.setLocal(default_local)
        }
        languagePrefManager!!.loadLocal()
    }
}