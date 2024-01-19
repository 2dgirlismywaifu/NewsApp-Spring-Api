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

class SaveThemeSettings(private var _context: Context) {
    private var pref: SharedPreferences = _context.getSharedPreferences("Theme", Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = pref.edit()

    fun saveTheme(theme: String?) {
        editor.putString("theme", theme)
        editor.apply()
    }
}