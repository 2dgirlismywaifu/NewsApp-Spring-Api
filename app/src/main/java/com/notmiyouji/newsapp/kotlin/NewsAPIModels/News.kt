package com.notmiyouji.newsapp.kotlin.NewsAPIModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class News {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("totalResult")
    @Expose
    var totalResult = 0

    @SerializedName("articles")
    @Expose
    var article: List<Article>? = null

    @SerializedName("countrylist")
    @Expose
    var countrylist: List<Country>? = null

    @SerializedName("countrycode")
    @Expose
    var countrycode: List<Country>? = null
}