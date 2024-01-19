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

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.java.activity.SourceNewsDetails
import com.notmiyouji.newsapp.kotlin.LoadImageURL
import com.notmiyouji.newsapp.kotlin.model.NewsSource

class ListSourceAdapter(var activity: AppCompatActivity, private var newsSourceList: List<NewsSource>) :
    RecyclerView.Adapter<ListSourceAdapter.ListSourceHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSourceHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.source_news_list, parent, false)
        return ListSourceHolder(view)
    }

    override fun onBindViewHolder(holder: ListSourceHolder, position: Int) {
        val newsSource = newsSourceList[position]
        holder.sourceName.text = newsSource.sourceName
        holder.sourceDescription.text = newsSource.sourceUrl
        val path = newsSource.image
        val loadImageURL = LoadImageURL(path)
        loadImageURL.getImageFromURL(holder.sourceImage, holder)
        holder.detailsBtn.setOnClickListener {
            val intent = Intent(activity, SourceNewsDetails::class.java)
            intent.putExtra("sourceId", newsSource.sourceId)
            intent.putExtra("sourceName", newsSource.sourceName)
            intent.putExtra("sourceUrl", newsSource.sourceUrl)
            intent.putExtra("sourceDescription", newsSource.information)
            intent.putExtra("sourceImage", newsSource.image)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return newsSourceList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(newsSourceList: List<NewsSource>) {
        this.newsSourceList = newsSourceList
        notifyDataSetChanged()
    }

    class ListSourceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sourceName: TextView
        var sourceDescription: TextView
        var sourceImage: ImageView
        var detailsBtn: Button

        init {
            sourceName = itemView.findViewById(R.id.source_name)
            sourceDescription = itemView.findViewById(R.id.source_description)
            sourceImage = itemView.findViewById(R.id.imgNews)
            detailsBtn = itemView.findViewById(R.id.button_details)
        }
    }
}
