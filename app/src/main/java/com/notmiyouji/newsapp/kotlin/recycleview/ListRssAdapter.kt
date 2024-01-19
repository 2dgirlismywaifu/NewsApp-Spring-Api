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
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.kotlin.util.LoadUrlImage
import com.notmiyouji.newsapp.kotlin.model.rss.RSSList

class ListRssAdapter(var activity: AppCompatActivity, private var rssSourceList: List<RSSList>) :
    RecyclerView.Adapter<ListRssAdapter.ListSourceHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSourceHolder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.source_news_details_layout, parent, false)
        return ListSourceHolder(view)
    }

    override fun onBindViewHolder(holder: ListSourceHolder, position: Int) {
        val rssList = rssSourceList[position]
        val title = activity.getString(R.string.list_rss_type_title) + rssList.urlType
        holder.rssTitle.text = title
        val url = activity.getString(R.string.rss_url) + rssList.url
        holder.rssUrl.text = url
        val path = rssList.urlImage
        val loadUrlImage = LoadUrlImage(path)
        loadUrlImage.getImageFromURL(holder.sourceImage, holder)
        //Picasso.get().load(path).into(holder.sourceImage);
    }

    override fun getItemCount(): Int {
        return rssSourceList.size
    }

    class ListSourceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rssTitle: TextView
        var rssUrl: TextView
        var sourceImage: ImageView

        init {
            rssTitle = itemView.findViewById(R.id.rss_type)
            rssUrl = itemView.findViewById(R.id.rss_url)
            sourceImage = itemView.findViewById(R.id.imgNews)
        }
    }
}