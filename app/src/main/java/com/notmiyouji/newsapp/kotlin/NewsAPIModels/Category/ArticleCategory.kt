package com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.SourceCateory
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.ArticleCategory
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Article

class ArticleCategory {
    @SerializedName("source")
    @Expose
    var source: SourceCateory? = null

    @SerializedName("author")
    @Expose
    var author: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("urlToImage")
    @Expose
    var urlToImage: String? = null

    @SerializedName("publishedAt")
    @Expose
    var publishedAt: String? = null
}