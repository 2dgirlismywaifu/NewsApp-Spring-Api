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

class SaveUserLogined(context: Context) {
    var sharedPreferences: SharedPreferences
    var editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences("UserLogined", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun saveUserLogined(
        userID: String?,
        fullname: String?,
        email: String?,
        password: String?,
        username: String?,
        avatar: String?,
        status: String?
    ) {
        editor.putString("userID", userID)
        editor.putString("fullname", fullname)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putString("username", username)
        editor.putString("avatar", avatar)
        editor.putString("status", status)
        editor.apply()
    }

    fun saveUserPassword(password: String?) {
        editor.putString("password", password)
        editor.apply()
    }

    fun saveFullname(fullname: String?) {
        editor.putString("fullname", fullname)
        editor.apply()
    }

    fun saveUsername(username: String?) {
        editor.putString("username", username)
        editor.apply()
    }

    fun saveAvatar(avatar: String?) {
        editor.putString("avatar", avatar)
        editor.apply()
    }

    fun saveGender(gender: String?) {
        editor.putString("gender", gender)
        editor.apply()
    }

    fun saveBirthday(birthday: String?) {
        editor.putString("birthday", birthday)
        editor.apply()
    }
}