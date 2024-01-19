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
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.java.activity.userlogin.NewsFavouriteByUser
import com.notmiyouji.newsapp.kotlin.LoadImageURL
import com.notmiyouji.newsapp.kotlin.activity.OpenNewsDetails
import com.notmiyouji.newsapp.kotlin.model.NewsFavourite
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin
import java.util.Objects

class NewsFavouriteAdapter(
    private var items: MutableList<NewsFavourite>,
    private val activity: AppCompatActivity
) : RecyclerView.Adapter<NewsFavouriteAdapter.FeedViewVerticalHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(activity)
    private val getUserLogin: GetUserLogin = GetUserLogin(activity)
    private var newsFavouriteByUser: NewsFavouriteByUser = NewsFavouriteByUser(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewVerticalHolder {
        val itemView = inflater.inflate(R.layout.news_items_vertical, parent, false)
        return FeedViewVerticalHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: FeedViewVerticalHolder, position: Int) {
        holder.txtTitle.text = items[position].title
        holder.txtPubDate.text = items[position].pubDate
        val path = items[position].imageUrl
        val loadImageURL = LoadImageURL(path)
        loadImageURL.getImageFromURL(holder.imageView, holder)
        if (getUserLogin.status == "login") {
            newsFavouriteByUser.checkFavouriteNewsInRecycleViewByUser(
                getUserLogin.userID,
                items[position].title,
                holder.favouriteBtn, holder.unFavouriteBtn
            )
        } else {
            holder.favouriteBtn.visibility = View.VISIBLE
            holder.unFavouriteBtn.visibility = View.GONE
        }
        holder.unFavouriteBtn.setOnClickListener {
            if (getUserLogin.status == "login") {
                newsFavouriteByUser.removeFavouriteByUser(
                    getUserLogin.userID,
                    items[position].favouriteId, items[position].title
                )
                holder.favouriteBtn.visibility = View.VISIBLE
                holder.unFavouriteBtn.visibility = View.GONE
                notifyDataSetChanged()
            } else {
                Toast.makeText(
                    activity,
                    R.string.please_login_to_use_this_feature,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        holder.itemView.setOnClickListener {
            val getPath: String = Objects.requireNonNullElse(
                path, "https://techvccloud.mediacdn.vn/2018/3/29/notavailableen-1522298007107364895792-21-0-470-800-crop-152229801290023105615.png"
            )
            //Web view is cool but it's not the best way to show the news, so I'm going to use a Chrome Custom Tab
            //If your browser not installed, it will open in the web-view
            val openNewsDetails = OpenNewsDetails(
                items[position].url,
                items[position].title,
                getPath,
                items[position].pubDate, activity
            )
            openNewsDetails.openNewsDetails()
        }
    }

    override fun getItemCount(): Int {
        return try {
            items.size
        } catch (e: NullPointerException) {
            e.printStackTrace()
            0
        }
    }

    fun clear() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(newsFavouriteList: MutableList<NewsFavourite>) {
        items = newsFavouriteList
        notifyDataSetChanged()
    }

    class FeedViewVerticalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView
        var txtPubDate: TextView
        private var txtSource: TextView
        var imageView: ImageView
        var favouriteBtn: ImageView
        var unFavouriteBtn: ImageView
        var activity: Activity? = null

        init {
            txtTitle = itemView.findViewById(R.id.txtTitle)
            txtPubDate = itemView.findViewById(R.id.txtPubDate)
            txtSource = itemView.findViewById(R.id.txtSource)
            imageView = itemView.findViewById(R.id.imgNews)
            favouriteBtn = itemView.findViewById(R.id.favouriteBtn)
            unFavouriteBtn = itemView.findViewById(R.id.unfavouriteBtn)
            //Set Event
        }
    }
}
