package com.notmiyouji.newsapp.kotlin.RSSSource

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewsDetails {
    @SerializedName("source_id")
    @Expose
    var source_id: String? = null

    @SerializedName("url_type")
    @Expose
    var url_type: String? = null

    @SerializedName("url")
    @Expose
    var urllink: String? = null
}