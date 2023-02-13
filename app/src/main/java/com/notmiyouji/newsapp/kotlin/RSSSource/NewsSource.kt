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

class NewsSource {
    @SerializedName("source_id")
    @Expose
    var source_id: String? = null

    @SerializedName("source_name")
    @Expose
    var source_name: String? = null

    @SerializedName("source_url")
    @Expose
    var source_url: String? = null

    @SerializedName("information")
    @Expose
    var information: String? = null

    @SerializedName("imgae")
    @Expose
    var imgae: String? = null
}