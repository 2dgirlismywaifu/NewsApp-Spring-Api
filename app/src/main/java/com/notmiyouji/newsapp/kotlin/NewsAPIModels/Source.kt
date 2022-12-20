package com.notmiyouji.newsapp.kotlin.NewsAPIModels

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.SourceCateory
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.ArticleCategory
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Article

class Source {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null
}