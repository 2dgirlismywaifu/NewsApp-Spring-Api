/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notmiyouji.newsapp.kotlin.SharedSettings

import androidx.appcompat.app.AppCompatActivity
import com.notmiyouji.newsapp.java.SharedSettings.LanguagePrefManager
import java.util.Locale

class LoadFollowLanguageSystem(private val activity: AppCompatActivity) {
    var languagePrefManager: LanguagePrefManager? = null
    fun loadLanguage() {
        languagePrefManager = LanguagePrefManager(activity)
        if( languagePrefManager!!.lang == "") {
            val default_local = Locale.getDefault().language.toString()
            languagePrefManager!!.setLocal(default_local)
        }
        else {
            languagePrefManager!!.setLocal(languagePrefManager!!.lang)
        }
        languagePrefManager!!.loadLocal()
    }
}