package com.notmiyouji.newsapp.kotlin.RSSSource

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListObject {
    @SerializedName("newsSource")
    @Expose
    var newsSource: List<NewsSource>? = null
    @SerializedName("newsDetails")
    @Expose
    var newsDetails: List<NewsDetails>? = null

}