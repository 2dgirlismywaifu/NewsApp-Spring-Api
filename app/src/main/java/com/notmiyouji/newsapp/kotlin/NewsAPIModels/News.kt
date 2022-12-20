package com.notmiyouji.newsapp.kotlin.NewsAPIModels

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.SourceCateory
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.ArticleCategory
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Article

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
}