package com.notmiyouji.newsapp.kotlin

import com.notmiyouji.newsapp.kotlin.NewsAPIModels.News
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPPInterface {
    @GET("newssource")
    fun getAllSource(): Call<ListObject?>?
    @GET("guest/newsdetails")
    fun getAllNewsDetails(
        @Query("type") type: String?,
        @Query("name") name: String?):
            Call<ListObject?>?
    @GET("newsapi/country/list")
    fun getListCountry(): Call<News?>?
    @GET("newsapi/country/list")
    fun getCountryCode(
        @Query("name") country: String?,
    ): Call<News?>?
    @GET("newsdetails/rss/list")
    fun getRSSList(
        @Query("name") name: String?,
    ): Call<ListObject?>?
    @GET("rss/convertIntoJson")
    fun getRSS2JSON(
        @Query("url", encoded = true) urlLink: String?,
    ): Call<ListObject?>?

}