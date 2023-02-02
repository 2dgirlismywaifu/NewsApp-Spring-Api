package com.notmiyouji.newsapp.kotlin.RetrofitInterface

import com.notmiyouji.newsapp.kotlin.LoginedModel.CheckNickName
import com.notmiyouji.newsapp.kotlin.LoginedModel.CountSSO
import com.notmiyouji.newsapp.kotlin.LoginedModel.Recovery
import com.notmiyouji.newsapp.kotlin.LoginedModel.Register
import com.notmiyouji.newsapp.kotlin.LoginedModel.SSO
import com.notmiyouji.newsapp.kotlin.LoginedModel.SignIn
import com.notmiyouji.newsapp.kotlin.LoginedModel.Verify
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.News
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject
import retrofit2.Call
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
        @Query("fullname", encoded = true) fullname: String?,
        @Query("email", encoded = true) email: String?,
        @Query("password", encoded = true) password: String?,
        @Query("nickname", encoded = true) nickname: String?
    ): Call<Register?>?

    @POST("sso")
    fun sso(
        @Query("fullname", encoded = true) fullname: String?,
        @Query("email", encoded = true) email: String?,
        @Query("nickname", encoded = true) nickname: String?,
        @Query("avatar", encoded = true) type: String?
    ): Call<SSO?>?

    @GET("/sso/count")
    fun ssoCount(
        @Query("account", encoded = true) email: String?
    ): Call<CountSSO?>?
    @GET("checknickname")
    fun checkNickname(
        @Query("nickname") nickname: String?,
        @Query("email") email: String?
    ): Call<CheckNickName?>?

    @POST("verify")
    fun verify(@Query("email", encoded = true) email: String?): Call<Verify?>?

    @GET("recoverycode")
    fun recoveryCode(@Query("email", encoded = true) email: String?): Call<Recovery?>?

    @GET("signin")
    fun signIn(
        @Query("account", encoded = true) account: String?,
        @Query("password", encoded = true) password: String?
    ): Call<SignIn?>?
}