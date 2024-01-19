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
import androidx.appcompat.app.AppCompatActivity

class GetUserLogin(var activity: AppCompatActivity) {
    private var sharedPreferences = activity.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)

    val userID: String?
        get() = sharedPreferences.getString("userID", "")
    val fullName: String?
        get() = sharedPreferences.getString("fullName", "")
    val email: String?
        get() = sharedPreferences.getString("email", "")
    val password: String?
        get() = sharedPreferences.getString("password", "")
    val username: String?
        get() = sharedPreferences.getString("username", "")
    val avatar: String?
        get() = sharedPreferences.getString("avatar", "")
    val gender: String?
        get() = sharedPreferences.getString("gender", "")
    val birthday: String?
        get() = sharedPreferences.getString("birthday", "")
    val status: String?
        get() = sharedPreferences.getString("status", "")
}