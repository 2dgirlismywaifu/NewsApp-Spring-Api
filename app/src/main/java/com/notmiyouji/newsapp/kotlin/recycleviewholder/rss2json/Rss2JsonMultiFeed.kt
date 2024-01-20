package com.notmiyouji.newsapp.kotlin.recycleviewholder.rss2json

import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.notmiyouji.newsapp.kotlin.model.NewsAppResult
import com.notmiyouji.newsapp.kotlin.model.rss2json.Result
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppApi.apiClient
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppInterface
import com.notmiyouji.newsapp.kotlin.util.AppUtils.encodeToBase64
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
class Rss2JsonMultiFeed(
    var activity: AppCompatActivity,
    var recyclerView: RecyclerView,
    var linearLayoutManager: LinearLayoutManager
) {
    private var newsAppInterface: NewsAppInterface = apiClient!!.create(NewsAppInterface::class.java)
    var call: Call<NewsAppResult?>? = null
    var items: MutableList<Result> = ArrayList()
    var adapterHorizontal: Rss2JsonAdapterHorizontal? = null
    var adapterVertical: Rss2JsonAdapterVertical? = null
    fun rss2JsonVertical(userId: String?, type: String?, alertDialog: AlertDialog) {
        call = newsAppInterface.convertRssUrl2Json(userId, encodeToBase64(type!!), "10")
        assert(call != null)
        call!!.enqueue(object : Callback<NewsAppResult?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<NewsAppResult?>,
                response: Response<NewsAppResult?>
            ) {
                if (response.isSuccessful) {
                    assert(response.body() != null)
                    if (response.body()!!.result != null) {
                        if (items.isNotEmpty()) {
                            //Clear the list
                            items.clear()
                        }
                        items = response.body()!!.result as MutableList<Result>
                        adapterVertical = Rss2JsonAdapterVertical(items, activity)
                        recyclerView.removeAllViews()
                        recyclerView.layoutManager = linearLayoutManager
                        recyclerView.adapter = adapterVertical
                        recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
                            if (adapterVertical!!.itemCount > 0) {
                                alertDialog.dismiss()
                            }
                        }
                        adapterVertical!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<NewsAppResult?>, t: Throwable) {
                println("Failure")
            }
        })
    }

    fun rss2JsonVerticalByKeyWord(
        userId: String?,
        keyword: String?,
        type: String?,
        alertDialog: AlertDialog
    ) {
        call = newsAppInterface.searchNewsFromRss(userId, keyword, encodeToBase64(type!!), "5")
        assert(call != null)
        call!!.enqueue(object : Callback<NewsAppResult?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<NewsAppResult?>,
                response: Response<NewsAppResult?>
            ) {
                if (response.isSuccessful) {
                    assert(response.body() != null)
                    if (response.body()!!.result != null) {
                        if (items.isNotEmpty()) {
                            items.clear()
                        }
                        items = response.body()!!.result as MutableList<Result>
                        adapterVertical = Rss2JsonAdapterVertical(items, activity)
                        recyclerView.removeAllViews()
                        recyclerView.layoutManager = linearLayoutManager
                        recyclerView.adapter = adapterVertical
                        recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
                            if (adapterVertical!!.itemCount > 0) {
                                alertDialog.dismiss()
                            }
                        }
                        adapterVertical!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<NewsAppResult?>, t: Throwable) {
                println("Failure")
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAdapterVertical() {
        adapterVertical = Rss2JsonAdapterVertical(items, activity)
        adapterVertical!!.clear()
        adapterVertical!!.notifyDataSetChanged()
    }

    fun rss2JsonHorizontal(userId: String?, type: String?, alertDialog: AlertDialog) {
        call = newsAppInterface.convertRssUrl2Json(userId, encodeToBase64(type!!), "5")
        assert(call != null)
        call!!.enqueue(object : Callback<NewsAppResult?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<NewsAppResult?>,
                response: Response<NewsAppResult?>
            ) {
                if (response.isSuccessful) {
                    assert(response.body() != null)
                    if (response.body()!!.result != null) {
                        if (items.isNotEmpty()) {
                            items.clear()
                        }
                        items = response.body()!!.result as MutableList<Result>
                        adapterHorizontal = Rss2JsonAdapterHorizontal(items, activity)
                        recyclerView.removeAllViews()
                        recyclerView.layoutManager = linearLayoutManager
                        recyclerView.adapter = adapterHorizontal
                        recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
                            if (adapterHorizontal!!.itemCount > 0) {
                                alertDialog.dismiss()
                            }
                        }
                        adapterHorizontal!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<NewsAppResult?>, t: Throwable) {
                println("Failure")
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAdapterHorizontal() {
        adapterHorizontal = Rss2JsonAdapterHorizontal(items, activity)
        adapterHorizontal!!.clear()
        adapterHorizontal!!.notifyDataSetChanged()
    }
}
