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
package com.notmiyouji.newsapp.kotlin.recycleviewholder.category

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.notmiyouji.newsapp.kotlin.recycleviewholder.rss2json.Rss2JsonMultiFeed

class RssUrlCategory(
    private val activity: AppCompatActivity,
    private val newsViewVertical: RecyclerView,
    private val alertDialog: AlertDialog,
    private val userId: String
) {
    private var newsViewLayoutVertical: LinearLayoutManager? = null
    fun startLoad(type: String?) {
        val loadNewsVerticalMode = Thread {
            newsViewLayoutVertical =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val feedMultiRSS =
                Rss2JsonMultiFeed(activity, newsViewVertical, newsViewLayoutVertical!!)
            feedMultiRSS.rss2JsonVertical(userId, type, alertDialog)
        }
        loadNewsVerticalMode.start()
    }
}
