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

package com.notmiyouji.newsapp.kotlin.sharedsettings

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceSettings(var context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("Wallpaper", Context.MODE_PRIVATE)
    private var sourcePrefs: SharedPreferences =
        context.getSharedPreferences("SourceName", Context.MODE_PRIVATE)
    private var countryPrefs: SharedPreferences =
        context.getSharedPreferences("CountryCode", Context.MODE_PRIVATE)
    private var langPrefs: SharedPreferences =
        context.getSharedPreferences("LangCode", Context.MODE_PRIVATE)

    fun getSharedWallpaperHeader(path: Int) {
        val editor = prefs.edit()
        editor.putInt("path", path)
        editor.apply()
    }

    fun getSharedSource(name: String) {
        val editor = sourcePrefs.edit()
        editor.putString("name", name)
        editor.apply()
    }

    fun getSharedCountry(code: String) {
        val editor = countryPrefs.edit()
        editor.putString("code", code)
        editor.apply()
    }

    fun getSharedLanguage(code: String) {
        val editor = langPrefs.edit()
        editor.putString("lang", code)
        editor.apply()
    }
}