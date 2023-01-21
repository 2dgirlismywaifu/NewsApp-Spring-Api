package com.notmiyouji.newsapp.kotlin.RetrofitInterface

import com.notmiyouji.newsapp.kotlin.RSSFeed.Category.CategoryType
import com.notmiyouji.newsapp.kotlin.RSSFeed.RSS2JSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RSS2JSONInterface {
    @GET("api.json")
    fun getRSS2JSON(
        @Query("rss_url") url: String?,
        @Query("api_key") api_key: String?
    ): Call<RSS2JSON?>?
    @GET("api.json")
    fun getCategoryType(
        @Query("rss_url") url: String?,
        @Query("api_key") api_key: String?
    ): Call<CategoryType?>?
}