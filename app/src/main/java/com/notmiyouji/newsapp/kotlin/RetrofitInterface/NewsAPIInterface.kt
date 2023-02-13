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

package com.notmiyouji.newsapp.kotlin.RetrofitInterface

import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.NewsCategory
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIInterface {
    @GET("top-headlines")
    fun getLatestNews(
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?
    ): Call<News?>?

    @GET("top-headlines")
    fun getNewsCategory(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("apiKey") apiKey: String?
    ): Call<NewsCategory?>?

    @GET("everything")
    fun getNewsSearch(
        @Query("q") keyword: String?,
        @Query("language") language: String?,
        @Query("sortBy") sortBy: String?,
        @Query("apiKey") apiKey: String?
    ): Call<News?>?
}