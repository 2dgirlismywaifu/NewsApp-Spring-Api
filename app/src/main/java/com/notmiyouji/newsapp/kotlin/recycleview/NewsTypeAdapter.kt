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
package com.notmiyouji.newsapp.kotlin.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.java.activity.MaterialAltertLoading
import com.notmiyouji.newsapp.kotlin.recycleviewholder.category.RssUrlCategory

class NewsTypeAdapter(private val activity: AppCompatActivity, private val userId: String) :
    RecyclerView.Adapter<NewsTypeAdapter.NewsTypeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsTypeHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.news_type, parent, false)
        return NewsTypeHolder(view)
    }

    override fun onBindViewHolder(holder: NewsTypeHolder, position: Int) {
        val data: Map<String, String> = setNewsType(activity)
        holder.newsType.text = data.keys.toTypedArray()[position]
        holder.newsType.setOnClickListener {
            val materialAlertLoading = MaterialAltertLoading(activity)
            val mDialog = materialAlertLoading.dialog
            val alertDialog = mDialog.create()
            alertDialog.show()
            val category = data[data.keys.toTypedArray()[position]]
            val rssURLCategory =
                RssUrlCategory(
                    activity,
                    activity.findViewById(R.id.cardnews_view_vertical),
                    alertDialog,
                    userId
                )
            rssURLCategory.startLoad(category)
        }
    }

    override fun getItemCount(): Int {
        return setNewsType(activity).size
    }

    private fun setNewsType(activity: AppCompatActivity): HashMap<String, String> {
        val data = HashMap<String, String>()
        val context = activity.baseContext
        data[context.getString(R.string.breakingnews_type)] = "BreakingNews"
        data[context.getString(R.string.worldnews_type)] = "World"
        data[context.getString(R.string.news_type)] = "News"
        data[context.getString(R.string.sportnews_type)] = "Sport"
        data[context.getString(R.string.lawnews_type)] = "Law"
        data[context.getString(R.string.educationnews_type)] = "Education"
        data[context.getString(R.string.healthnews_type)] = "Health"
        data[context.getString(R.string.lifestylenews_type)] = "LifeStyle"
        data[context.getString(R.string.travelnews_type)] = "Travel"
        data[context.getString(R.string.sciencenews_type)] = "Science"
        return data
    }

    class NewsTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newsType: Button

        init {
            newsType = itemView.findViewById(R.id.news_type_text)
        }
    }
}
