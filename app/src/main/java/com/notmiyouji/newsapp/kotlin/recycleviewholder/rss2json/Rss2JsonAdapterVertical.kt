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
package com.notmiyouji.newsapp.kotlin.recycleviewholder.rss2json

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
import com.notmiyouji.newsapp.kotlin.activity.OpenNewsDetails
import com.notmiyouji.newsapp.kotlin.model.rss2json.Result
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin
import com.notmiyouji.newsapp.kotlin.util.LoadUrlImage
import com.notmiyouji.newsapp.kotlin.util.RegExImageFromDescription
import java.util.Objects

class Rss2JsonAdapterVertical(
    private val items: MutableList<Result>,
    private val activity: AppCompatActivity) :
    RecyclerView.Adapter<Rss2JsonAdapterVertical.FeedViewVerticalHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(activity)
    private val getUserLogin: GetUserLogin = GetUserLogin(activity)
    private val newsFavouriteByUser: NewsFavouriteByUser = NewsFavouriteByUser(activity)
    var name: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewVerticalHolder {
        val itemView = inflater.inflate(R.layout.news_items_vertical, parent, false)
        return FeedViewVerticalHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: FeedViewVerticalHolder, position: Int) {
        holder.txtTitle.text = items[position].title
        holder.txtPubDate.text = items[position].pubDate
        holder.txtsource.text = name
        val path: String?
        val description = items[position].description
        path = if (description != null) {
            val regExImageFromDescription = RegExImageFromDescription(description)
            regExImageFromDescription.regEXImage()
        } else {
            null
        }
        val loadUrlImage = LoadUrlImage(path)
        loadUrlImage.getImageFromURL(holder.imageView, holder)
        if (getUserLogin.status == "login") {
            newsFavouriteByUser.checkFavouriteNewsInRecycleViewByUser(
                getUserLogin.userID,
                items[position].title,
                holder.favouritebtn, holder.unfavouritebtn
            )
        } else {
            holder.favouritebtn.visibility = View.VISIBLE
            holder.unfavouritebtn.visibility = View.GONE
        }
        holder.favouritebtn.setOnClickListener {
            if (getUserLogin.status == "login") {
                newsFavouriteByUser.addFavouriteByUser(
                    getUserLogin.userID,
                    items[position].link,
                    items[position].title,
                    path, items[position].pubDate
                )
                holder.favouritebtn.visibility = View.GONE
                holder.unfavouritebtn.visibility = View.VISIBLE
            } else {
                Toast.makeText(activity, "Please login to use this feature", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        holder.unfavouritebtn.setOnClickListener {
            if (getUserLogin.status == "login") {
                newsFavouriteByUser.removeFavouriteByUser(
                    getUserLogin.userID,
                    "",
                    items[position].title
                )
                holder.favouritebtn.visibility = View.VISIBLE
                holder.unfavouritebtn.visibility = View.GONE
            } else {
                Toast.makeText(activity, "Please login to use this feature", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        holder.itemView.setOnClickListener {
            //Web view is cool but it's not the best way to show the news, so I'm going to use a Chrome Custom Tab
            //If your browser not installed, it will open in the webview
            val getPath: String = Objects.requireNonNullElse(path,
                "https://techvccloud.mediacdn.vn/2018/3/29/notavailableen-1522298007107364895792-21-0-470-800-crop-152229801290023105615.png"
            )
            val openNewsDetails = OpenNewsDetails(
                items[position].link,
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

    class FeedViewVerticalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView
        var txtPubDate: TextView
        var txtsource: TextView
        var imageView: ImageView
        var favouritebtn: ImageView
        var unfavouritebtn: ImageView
        var activity: Activity? = null

        init {
            txtTitle = itemView.findViewById(R.id.txtTitle)
            txtPubDate = itemView.findViewById(R.id.txtPubDate)
            txtsource = itemView.findViewById(R.id.txtSource)
            imageView = itemView.findViewById(R.id.imgNews)
            favouritebtn = itemView.findViewById(R.id.favouriteBtn)
            unfavouritebtn = itemView.findViewById(R.id.unfavouriteBtn)
            //Set Event
        }
    }
}
