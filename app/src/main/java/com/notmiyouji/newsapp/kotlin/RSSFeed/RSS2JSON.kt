package com.notmiyouji.newsapp.kotlin.RSSFeed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RSS2JSON {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("feed")
    @Expose
    var feed: Feed? = null

    @SerializedName("items")
    @Expose
    var items: List<Items>? = null
}