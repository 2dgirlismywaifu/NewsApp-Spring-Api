package com.notmiyouji.newsapp.kotlin.RSSFeed.Category

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CategoryType {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("feed")
    @Expose
    var feedCategory: FeedCategory? = null

    @SerializedName("items")
    @Expose
    var itemsCategory: List<ItemsCategory>? = null
}