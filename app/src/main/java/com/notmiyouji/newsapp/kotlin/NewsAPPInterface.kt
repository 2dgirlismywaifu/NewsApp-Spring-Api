package com.notmiyouji.newsapp.kotlin

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

}