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