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

class NewsFavourite {
    // SerializedName and Expose: sync_id, user_id, url, title, image_url, source_name, status
    @SerializedName("favouriteId")
    @Expose
    var favouriteId: String? = null

    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null

    @SerializedName("pubDate")
    @Expose
    var pubDate: String? = null

    @SerializedName("sourceName")
    @Expose
    var sourceName: String? = null

    @SerializedName("isDeleted")
    @Expose
    var isDeleted: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}