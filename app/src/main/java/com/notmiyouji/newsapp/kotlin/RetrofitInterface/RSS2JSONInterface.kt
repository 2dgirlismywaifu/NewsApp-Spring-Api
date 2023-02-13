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

import com.notmiyouji.newsapp.kotlin.RSSFeed.Category.CategoryType
import com.notmiyouji.newsapp.kotlin.RSSFeed.RSS2JSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RSS2JSONInterface {
    @GET("api.json")
    fun getRSS2JSON(
        @Query("rss_url") url: String?,
        @Query("api_key") api_key: String?
    ): Call<RSS2JSON?>?

    @GET("api.json")
    fun getCategoryType(
        @Query("rss_url") url: String?,
        @Query("api_key") api_key: String?
    ): Call<CategoryType?>?
}