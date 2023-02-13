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

package com.notmiyouji.newsapp.kotlin.OpenActivity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.notmiyouji.newsapp.java.Global.SettingsPage
import com.notmiyouji.newsapp.java.Signed.SettingsLogined
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined

class OpenSettingsPage(var activity: AppCompatActivity) {
    var sharedPreferences: SharedPreferences? = null
    var intent: Intent? = null
    fun openSettings() {
        val getUserLogined = GetUserLogined(activity)
        when (getUserLogined.status) {
            "login", "google" -> {
                intent = Intent(activity, SettingsLogined::class.java)
                activity.startActivity(intent)
            }
            else -> {
                intent = Intent(activity, SettingsPage::class.java)
                activity.startActivity(intent)
            }
        }
    }
}