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

package com.notmiyouji.newsapp.kotlin.RSSFeed.Category

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ItemsCategory {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("pubDate")
    @Expose
    var pubDate: String? = null

    @SerializedName("link")
    @Expose
    var link: String? = null

    @SerializedName("guid")
    @Expose
    var guid: String? = null

    @SerializedName("author")
    @Expose
    var author: String? = null

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("content")
    @Expose
    var content: String? = null

    @SerializedName("enclosure")
    @Expose
    var enclosureCategory: EnclosureCategory? = null

    @SerializedName("categories")
    @Expose
    var categories: List<Any>? = null
}