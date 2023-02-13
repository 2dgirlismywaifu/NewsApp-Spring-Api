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

package com.notmiyouji.newsapp.kotlin.FavouriteModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class NewsFavouriteShow {
    //favorite_id, user_id, url, title, image_url, source_name
    @SerializedName("favorite_id")
    @Expose
    var favoriteId: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null
    @SerializedName("pub_date")
    @Expose
    var pubdate: String? = null
    @SerializedName("source_name")
    @Expose
    var sourceName: String? = null
}