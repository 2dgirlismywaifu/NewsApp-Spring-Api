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

import android.content.Context
import android.content.SharedPreferences

class LanguagePrefManager(var context: Context) {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor

    // Shared preferences file name
    init {
        pref = context.getSharedPreferences("LangCode", Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    fun setLocal(lang: String?) {
        //Save config by shared preferences
        val editor = pref.edit()
        editor.putString("lang", lang)
        editor.apply()
    }

    val lang: String?
        get() = pref.getString("lang", "")

    fun loadLocal() {
        setLocal(lang)
    }
}