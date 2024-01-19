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

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.kotlin.util.LoadUrlImage

class LoadNavigationHeader(
    var activity: AppCompatActivity,
    private var navigationView: NavigationView
) {
    @SuppressLint("SetTextI18n")
    fun loadHeader() {
        val sharedPreferences = activity.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
        //Get String from sharedPreference
        val fullName = sharedPreferences.getString("fullName", "")
        val username = sharedPreferences.getString("username", "")
        val avatar = sharedPreferences.getString("avatar", "")
        val status = sharedPreferences.getString("status", "")
        when {
            status.equals("login") || status.equals("google") -> {
                navigationView.inflateHeaderView(R.layout.navigation_header_logined)
                val fullNameHeader = navigationView.getHeaderView(0).findViewById<TextView>(R.id.fullname)
                fullNameHeader.text = fullName
                val usernameHeader = navigationView.getHeaderView(0).findViewById<TextView>(R.id.user_name)
                usernameHeader.text = "@$username"
                val avatarHeader = navigationView.getHeaderView(0)
                        .findViewById<ShapeableImageView>(R.id.avatar_user)
                //Call LoadImageURL.kt
                val loadUrlImage = LoadUrlImage(avatar)
                loadUrlImage.loadImageUser(avatarHeader)
            }

            else -> {
                //load default header

                navigationView.inflateHeaderView(R.layout.navigation_header)
            }
        }
    }
}