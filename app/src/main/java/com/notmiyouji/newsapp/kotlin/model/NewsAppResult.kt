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

package com.notmiyouji.newsapp.kotlin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.notmiyouji.newsapp.kotlin.model.newsapi.Article
import com.notmiyouji.newsapp.kotlin.model.newsapi.Country
import com.notmiyouji.newsapp.kotlin.model.rss.NewsDetails
import com.notmiyouji.newsapp.kotlin.model.rss.NewsFavourite
import com.notmiyouji.newsapp.kotlin.model.rss.NewsSource
import com.notmiyouji.newsapp.kotlin.model.rss.RSSList
import com.notmiyouji.newsapp.kotlin.model.rss.Result

class NewsAppResult {
    @SerializedName("newsSource")
    @Expose
    var newsSource: List<NewsSource>? = null

    @SerializedName("newsDetails")
    @Expose
    var newsDetails: List<NewsDetails>? = null

    @SerializedName("newsFavourite")
    @Expose
    var newsFavouriteList: List<NewsFavourite>? = null

    @SerializedName("ListOfRSS")
    @Expose
    var listRss: List<RSSList>? = null

    @SerializedName("articles")
    @Expose
    var article: List<Article>? = null

    @SerializedName("countryList")
    @Expose
    var countryList: List<Country>? = null

    @SerializedName("countryCode")
    @Expose
    var countryCode: String? = null

    //News from Rss to Json (Using RssReader Dependency in backend)
    @SerializedName("result")
    @Expose
    var result: List<Result>? = null
}