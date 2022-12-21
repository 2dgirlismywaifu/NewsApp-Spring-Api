package com.notmiyouji.newsapp.kotlin

import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.NewsCategory
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIInterface {
    @GET("top-headlines")
    fun getLatestNews(
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?
    ): Call<News?>?

    @GET("top-headlines")
    fun getNewsCategory(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("apiKey") apiKey: String?
    ): Call<NewsCategory?>?

    @GET("everything")
    fun getNewsSearch(
        @Query("q") keyword: String?,
        @Query("language") language: String?,
        @Query("sortBy") sortBy: String?,
        @Query("apiKey") apiKey: String?
    ): Call<News?>?
}