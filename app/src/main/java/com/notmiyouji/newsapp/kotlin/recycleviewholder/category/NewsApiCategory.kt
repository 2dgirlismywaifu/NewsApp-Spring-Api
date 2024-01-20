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

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.kotlin.model.NewsAppResult
import com.notmiyouji.newsapp.kotlin.model.newsapi.Article
import com.notmiyouji.newsapp.kotlin.recycleviewholder.newsapi.NewsAdapterVertical
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppApi.apiClient
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsApiCategory {
    private val newsAppInterface = apiClient!!.create(
        NewsAppInterface::class.java
    )
    private var articles: MutableList<Article> = ArrayList() //include category
    private var newsAdapterVertical: NewsAdapterVertical? = null
    private var callCategory: Call<NewsAppResult>? = null
    fun searchEveryThingByKeyWord(
        activity: AppCompatActivity,
        mDialog: AlertDialog,
        newsViewVertical: RecyclerView,
        keyWord: String?
    ) {
        val findEveryThing = Thread {
            callCategory =
                newsAppInterface.getEveryThingsNewsFromNewsApi(keyWord, "popularity", "5")
            assert(callCategory != null)
            callCategory!!.enqueue(object : Callback<NewsAppResult?> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    callCategory: Call<NewsAppResult?>,
                    response2: Response<NewsAppResult?>
                ) {
                    if (response2.isSuccessful) {
                        assert(response2.body() != null)
                        if (response2.body()!!.article != null) {
                            if (articles.isNotEmpty()) {
                                articles.clear()
                            }
                            articles = response2.body()!!.article as MutableList<Article>
                            val newsAPIVerticalLayout =
                                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                            newsViewVertical.layoutManager = newsAPIVerticalLayout
                            newsAdapterVertical = NewsAdapterVertical(articles, activity)
                            newsViewVertical.adapter = newsAdapterVertical
                            newsAdapterVertical!!.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<NewsAppResult?>, t: Throwable) {
                    Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT)
                        .show()
                }
            })
            activity.runOnUiThread { newsViewVertical.viewTreeObserver.addOnGlobalLayoutListener { mDialog.dismiss() } }
        }
        findEveryThing.start()
    }

    fun loadJsonCategory(
        activity: AppCompatActivity,
        mDialog: AlertDialog,
        categoryName: String?,
        newsViewVertical: RecyclerView,
        country: String?,
        keyWord: String?
    ) {
        val loadSourceGeneral = Thread {
            callCategory = newsAppInterface.getNewsTopHeadlinesFromNewsApi(
                keyWord,
                country,
                categoryName,
                "20"
            )
            assert(callCategory != null)
            callCategory!!.enqueue(object : Callback<NewsAppResult?> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    callCategory: Call<NewsAppResult?>,
                    response2: Response<NewsAppResult?>
                ) {
                    if (response2.isSuccessful) {
                        assert(response2.body() != null)
                        if (response2.body()!!.article != null) {
                            if (articles.isNotEmpty()) {
                                articles.clear()
                            }
                            articles = response2.body()!!.article as MutableList<Article>
                            val newsAPIVerticalLayout =
                                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                            newsViewVertical.layoutManager = newsAPIVerticalLayout
                            newsAdapterVertical = NewsAdapterVertical(articles, activity)
                            newsViewVertical.adapter = newsAdapterVertical
                            newsAdapterVertical!!.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<NewsAppResult?>, t: Throwable) {
                    Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT)
                        .show()
                }
            })
            activity.runOnUiThread { newsViewVertical.viewTreeObserver.addOnGlobalLayoutListener { mDialog.dismiss() } }
        }
        loadSourceGeneral.start()
    }
}
