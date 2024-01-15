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

package com.notmiyouji.newsapp.kotlin.activity

import android.app.ActivityOptions
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.java.activity.userlogin.SignInForm

class CallSignInForm(var navigationView: NavigationView, var activity: AppCompatActivity) {

    fun callSignInForm() {
        val headerView = navigationView.getHeaderView(0)
        val loginButton: Button = headerView.findViewById(R.id.SignInHeader)
        loginButton.setOnClickListener {
            val intent = Intent(activity, SignInForm::class.java)
            activity.startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
            )
        }
    }
}