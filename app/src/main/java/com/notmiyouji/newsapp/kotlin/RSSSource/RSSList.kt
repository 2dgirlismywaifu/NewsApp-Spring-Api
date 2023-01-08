package com.notmiyouji.newsapp.kotlin.RSSSource

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RSSList {
    @SerializedName("url_type")
    @Expose
    var url_type: String? = null

    @SerializedName("url")
    @Expose
    var urllink: String? = null

    @SerializedName("url_image")
    @Expose
    var url_image: String? = null
}