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