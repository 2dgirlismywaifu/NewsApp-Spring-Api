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
package com.notmiyouji.newsapp.kotlin.recycleviewholder.newsapi

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.java.activity.userlogin.NewsFavouriteByUser
import com.notmiyouji.newsapp.kotlin.activity.OpenNewsDetails
import com.notmiyouji.newsapp.kotlin.model.newsapi.Article
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin
import com.notmiyouji.newsapp.kotlin.util.AppUtils.dateToTimeFormat
import com.notmiyouji.newsapp.kotlin.util.LoadUrlImage
import java.util.Objects

class NewsAdapterHorizontal(articles: List<Article>, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<NewsAdapterHorizontal.MyViewHolder>() {
    private val getUserLogin: GetUserLogin = GetUserLogin(activity)
    private val newsFavouriteByUser: NewsFavouriteByUser = NewsFavouriteByUser(activity)
    private val articles: List<Article>

    init {
        this.articles = articles
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(activity).inflate(R.layout.news_items_horizontal, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holders: MyViewHolder, position: Int) {
        val model = articles[position]
        val path = model.urlToImage
        val loadUrlImage = LoadUrlImage(path)
        loadUrlImage.getImageFromURL(holders.imageView, holders)
        holders.title.text = model.title
        holders.source.text = model.source!!.name
        holders.time.text = " • " + dateToTimeFormat(model.publishedAt)
        if (Objects.requireNonNull<String?>(getUserLogin.status) == "login") {
            newsFavouriteByUser.checkFavouriteNewsInRecycleViewByUser(
                getUserLogin.userID, model.title,
                holders.favouriteBtn, holders.unFavouriteBtn
            )
        } else {
            holders.favouriteBtn.visibility = View.VISIBLE
            holders.unFavouriteBtn.visibility = View.GONE
        }
        holders.favouriteBtn.setOnClickListener {
            if (getUserLogin.status == "login") {
                newsFavouriteByUser.addFavouriteByUser(
                    getUserLogin.userID, model.url,
                    model.title,
                    path,
                    model.publishedAt
                )
                holders.favouriteBtn.visibility = View.GONE
                holders.unFavouriteBtn.visibility = View.VISIBLE
            } else {
                Toast.makeText(
                    activity,
                    R.string.please_login_to_use_this_feature,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        holders.unFavouriteBtn.setOnClickListener {
            if (getUserLogin.status == "login") {
                newsFavouriteByUser.removeFavouriteByUser(
                    getUserLogin.userID, "",
                    model.title
                )
                holders.favouriteBtn.visibility = View.VISIBLE
                holders.unFavouriteBtn.visibility = View.GONE
            } else {
                Toast.makeText(
                    activity,
                    R.string.please_login_to_use_this_feature,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        holders.itemView.setOnClickListener {
            val openNewsDetails = OpenNewsDetails(
                model.url,
                model.title,
                model.urlToImage,
                model.publishedAt, activity
            )
            openNewsDetails.openNewsDetails()
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var source: TextView
        var time: TextView
        var favouriteBtn: ImageView
        var unFavouriteBtn: ImageView
        var imageView: ShapeableImageView

        init {
            title = itemView.findViewById(R.id.txtTitle)
            source = itemView.findViewById(R.id.txtSource)
            time = itemView.findViewById(R.id.txtPubDate)
            imageView = itemView.findViewById(R.id.imgNews)
            favouriteBtn = itemView.findViewById(R.id.favouriteBtn)
            unFavouriteBtn = itemView.findViewById(R.id.unfavouriteBtn)
        }
    }
}
