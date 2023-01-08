package com.notmiyouji.newsapp.kotlin.RSSSource

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.notmiyouji.newsapp.kotlin.RSSFeed.Item

class ListObject {
    @SerializedName("newsSource")
    @Expose
    var newsSource: List<NewsSource>? = null

    @SerializedName("newsDetails")
    @Expose
    var newsDetails: List<NewsDetails>? = null

    @SerializedName("RSSList")
    @Expose
    var rssList: List<RSSList>? = null

    @SerializedName("item")
    @Expose
    var item: List<Item>? = null

}