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

import android.app.Activity
import android.content.Intent
import com.notmiyouji.newsapp.java.activity.newsdetail.NewsDetailByChrome
import com.notmiyouji.newsapp.java.activity.newsdetail.NewsDetailByWebView
import com.notmiyouji.newsapp.kotlin.sharedsettings.UseChromeShared

class OpenNewsDetails(
    private val url: String?,
    private val title: String?,
    private val img: String?,
    private val source: String?,
    private val pubdate: String?,
    private val activity: Activity
) {
    //function check img is null or not
    fun checkImg(): String {
        return if (img == null) {
            "https://i.imgur.com/1Z1Z1Z1.png"
        } else {
            img
        }
    }

    fun openNewsDetails() {
        when {
            UseChromeShared(activity).enableChrome -> {
                //Open in chrome custom tabs
                val chromeClient =
                    NewsDetailByChrome(
                        url,
                        title,
                        checkImg(),
                        url,
                        pubdate, activity
                    )
                chromeClient.openNewsDetails()
            }

            else -> {
                //No chrome installed, open in webview
                val intent = Intent(activity, NewsDetailByWebView::class.java)
                intent.putExtra("url", url)
                intent.putExtra("title", title)
                intent.putExtra("img", checkImg())
                intent.putExtra("source", source)
                intent.putExtra("pubdate", pubdate)
                activity.startActivity(intent)
            }
        }
    }
}