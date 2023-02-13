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

package com.notmiyouji.newsapp.kotlin.RSSSource

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.notmiyouji.newsapp.kotlin.FavouriteModel.NewsFavouriteShow

class ListObject() {
    @SerializedName("newsSource")
    @Expose
    var newsSource: List<NewsSource>? = null

    @SerializedName("newsDetails")
    @Expose
    var newsDetails: List<NewsDetails>? = null

    @SerializedName("RSSList")
    @Expose
    var rssList: List<RSSList>? = null

    @SerializedName("NewsFavourite")
    @Expose
    var newsFavouriteList: List<NewsFavouriteShow>? = null

}