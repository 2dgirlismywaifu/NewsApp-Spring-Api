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
import com.notmiyouji.newsapp.java.category.NewsApiCategory

class NewsAPITypeAdapter(private val activity: AppCompatActivity, private val country: String) :
    RecyclerView.Adapter<NewsAPITypeAdapter.NewsTypeHolder>() {
    private val newsAPICategory = NewsApiCategory()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsTypeHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.news_type, parent, false)
        return NewsTypeHolder(view)
    }

    override fun onBindViewHolder(holder: NewsTypeHolder, position: Int) {
        val data = setNewsType(activity)
        holder.newsType.text = data.keys.toTypedArray()[position]
        holder.newsType.setOnClickListener {
            //load Dialog
            val mDialog = MaterialAltertLoading(activity).dialog
            val dialog = mDialog.create()
            dialog.show()
            //fetch follow category
            val category = data[data.keys.toTypedArray()[position]]
            newsAPICategory.LoadJSONCategory(activity, dialog, category,
                activity.findViewById(R.id.cardnews_view_vertical), country, "")
        }
    }

    override fun getItemCount(): Int {
        return setNewsType(activity).size
    }

    private fun setNewsType(activity: AppCompatActivity): HashMap<String, String> {
        val data = HashMap<String, String>()
        val context = activity.baseContext
        data[context.getString(R.string.general_newsapi)] = "General"
        data[context.getString(R.string.business_newsapi)] = "Business"
        data[context.getString(R.string.entertainment_newsapi)] = "Entertainment"
        data[context.getString(R.string.health_newsapi)] = "Health"
        data[context.getString(R.string.science_newsapi)] = "Science"
        data[context.getString(R.string.sports_newsapi)] = "Sports"
        data[context.getString(R.string.technology_newsapi)] = "Technology"
        return data
    }

    class NewsTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsType: Button

        init {
            newsType = itemView.findViewById(R.id.news_type_text)
        }
    }
}
