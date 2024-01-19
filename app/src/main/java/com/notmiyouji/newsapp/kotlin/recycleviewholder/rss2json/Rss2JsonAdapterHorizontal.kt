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
import com.notmiyouji.newsapp.kotlin.recycleviewholder.rss2json.Rss2JsonAdapterHorizontal.FeedViewHolder
import com.notmiyouji.newsapp.kotlin.activity.OpenNewsDetails
import com.notmiyouji.newsapp.kotlin.model.rss2json.Result
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin
import com.notmiyouji.newsapp.kotlin.util.LoadUrlImage
import com.notmiyouji.newsapp.kotlin.util.RegExImageFromDescription
import java.util.Objects

class Rss2JsonAdapterHorizontal(
    private val items: MutableList<Result>,
    private val activity: AppCompatActivity) : RecyclerView.Adapter<FeedViewHolder>() {

        private val inflater: LayoutInflater = LayoutInflater.from(activity)
    private val getUserLogin: GetUserLogin = GetUserLogin(activity)
    private val newsFavouriteByUser: NewsFavouriteByUser = NewsFavouriteByUser(activity)
    var name: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = inflater.inflate(R.layout.news_items_horizontal, parent, false)
        return FeedViewHolder(itemView)
    }

    @SuppressLint("RtlHardcoded")
    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.txtTitle.text = items[position].title
        holder.txtPubDate.text = items[position].pubDate
        holder.txtSource.text = name
        val description = items[position].description
        val path: String? = if (description != null) {
            val regExImageFromDescription = RegExImageFromDescription(description)
            regExImageFromDescription.regEXImage()
        } else {
            null
        }
        val loadUrlImage = LoadUrlImage(path)
        loadUrlImage.getImageFromURL(holder.imageView, holder)
        if (Objects.requireNonNull(getUserLogin.status) == "login") {
            newsFavouriteByUser.checkFavouriteNewsInRecycleViewByUser(
                getUserLogin.userID,
                items[position].title,
                holder.favouriteBtn, holder.unFavouriteBtn
            )
        } else {
            holder.favouriteBtn.visibility = View.VISIBLE
            holder.unFavouriteBtn.visibility = View.GONE
        }
        holder.favouriteBtn.setOnClickListener {
            if (getUserLogin.status == "login") {
                newsFavouriteByUser.addFavouriteByUser(
                    getUserLogin.userID,
                    items[position].link,
                    items[position].title,
                    path, items[position].pubDate
                )
                holder.favouriteBtn.visibility = View.GONE
                holder.unFavouriteBtn.visibility = View.VISIBLE
            } else {
                Toast.makeText(
                    activity,
                    R.string.please_login_to_use_this_feature,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        holder.unFavouriteBtn.setOnClickListener {
            if (getUserLogin.status == "login") {
                newsFavouriteByUser.removeFavouriteByUser(
                    getUserLogin.userID,
                    "",
                    items[position].title
                )
                holder.favouriteBtn.visibility = View.VISIBLE
                holder.unFavouriteBtn.visibility = View.GONE
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
                path,
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
        return items.size
    }

    fun clear() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView
        var txtPubDate: TextView
        var txtSource: TextView
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
