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

class UseChromeShared(var context: Context) {
    var pref: SharedPreferences = context.getSharedPreferences(USE_CHROME, Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = pref.edit()

    var enableChrome: Boolean
        get() = pref.getBoolean(USE_CHROME_DEFAULT, false)
        set(value) {
            editor.putBoolean(USE_CHROME_DEFAULT, value)
            editor.commit()
        }

    companion object {
        const val USE_CHROME = "useChrome"
        const val USE_CHROME_DEFAULT = "useChromeDefault"
    }
}