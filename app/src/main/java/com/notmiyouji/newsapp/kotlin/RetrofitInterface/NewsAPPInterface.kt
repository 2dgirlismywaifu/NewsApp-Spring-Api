package com.notmiyouji.newsapp.kotlin.RetrofitInterface

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.notmiyouji.newsapp.kotlin.LoginedModel.CheckNickName
import com.notmiyouji.newsapp.kotlin.LoginedModel.Recovery
import com.notmiyouji.newsapp.kotlin.LoginedModel.Register
import com.notmiyouji.newsapp.kotlin.LoginedModel.Verify
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.News
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NewsAPPInterface {
    //Guest mode
    @GET("newssource")
    fun getAllSource(): Call<ListObject?>?

    @GET("guest/newsdetails")
    fun getAllNewsDetails(
        @Query("type") type: String?,
        @Query("name") name: String?
    ):
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

    @POST("register")
    fun register(
        @Query("email", encoded = true) email: String?,
        @Query("password", encoded = true) password: String?,
        @Query("nickname", encoded = true) nickname: String?
    ): Call<Register?>?
    @GET("checknickname")
    fun checkNickname(
        @Query("nickname") nickname: String?,
        @Query("email") email: String?
    ): Call<CheckNickName?>?
    @POST("verify")
    fun verify(@Query("email", encoded = true) email: String?): Call<Verify?>?
    @GET("recoverycode")
    fun recoveryCode(@Query("email", encoded = true) email: String?): Call<Recovery?>?
}