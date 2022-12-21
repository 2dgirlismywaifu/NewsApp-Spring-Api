package com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewsCategory {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("totalResult")
    @Expose
    var totalResult = 0

    @SerializedName("articles")
    @Expose
    var article: List<ArticleCategory>? = null
}