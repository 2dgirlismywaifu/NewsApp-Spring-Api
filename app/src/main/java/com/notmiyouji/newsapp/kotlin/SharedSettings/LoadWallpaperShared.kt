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
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.navigation.NavigationView
import com.notmiyouji.newsapp.R

class LoadWallpaperShared(//This class will load the wallpaper from shared preferences for header navigation
    navigationView: NavigationView, var activity: AppCompatActivity
) {
    private var headerNavigation: View = navigationView.getHeaderView(0)
    private var header_guest: LinearLayout =
        headerNavigation.findViewById<View>(R.id.header_guest) as LinearLayout
    val prefs = activity.getSharedPreferences("Wallpaper", Context.MODE_PRIVATE)

    private fun requestBackground(): Int {
        val backgroundcode = prefs.getInt(
            "path", header_guest.background.current.constantState!!.changingConfigurations
        )
        return backgroundcode
    }

    fun loadWallpaper() {
        if (requestBackground() != header_guest.background.current.constantState!!.changingConfigurations) {
            header_guest.background =
                ResourcesCompat.getDrawable(activity.resources, requestBackground(), null)
        }
    }
}