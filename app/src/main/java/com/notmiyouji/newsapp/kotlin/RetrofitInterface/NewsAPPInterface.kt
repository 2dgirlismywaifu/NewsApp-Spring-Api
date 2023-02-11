package com.notmiyouji.newsapp.kotlin.RetrofitInterface

import com.notmiyouji.newsapp.kotlin.FavouriteModel.SourceSubscribe
import com.notmiyouji.newsapp.kotlin.FavouriteModel.NewsFavouriteShow
import com.notmiyouji.newsapp.kotlin.FavouriteModel.SourceUnsubscribe
import com.notmiyouji.newsapp.kotlin.FavouriteModel.NewsFavourite
import com.notmiyouji.newsapp.kotlin.FavouriteModel.NewsUnfavourite
import com.notmiyouji.newsapp.kotlin.FavouriteModel.SourceSubscribeCheck
import com.notmiyouji.newsapp.kotlin.LoginedModel.CheckNickName
import com.notmiyouji.newsapp.kotlin.LoginedModel.CountSSO
import com.notmiyouji.newsapp.kotlin.LoginedModel.Recovery
import com.notmiyouji.newsapp.kotlin.LoginedModel.Register
import com.notmiyouji.newsapp.kotlin.LoginedModel.SSO
import com.notmiyouji.newsapp.kotlin.LoginedModel.SignIn
import com.notmiyouji.newsapp.kotlin.LoginedModel.UpdateSSO
import com.notmiyouji.newsapp.kotlin.LoginedModel.Verify
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.News
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject
import com.notmiyouji.newsapp.kotlin.UpdateModel.Birthday
import com.notmiyouji.newsapp.kotlin.UpdateModel.CheckPassword
import com.notmiyouji.newsapp.kotlin.UpdateModel.FullName
import com.notmiyouji.newsapp.kotlin.UpdateModel.Gender
import com.notmiyouji.newsapp.kotlin.UpdateModel.Password
import com.notmiyouji.newsapp.kotlin.UpdateModel.RecoveryCode
import com.notmiyouji.newsapp.kotlin.UpdateModel.UserName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NewsAPPInterface {
    //Guest mode
    @GET("newssource")
    fun getAllSource(): Call<ListObject?>?
    @GET("account/newssource")
    fun accountAllSource(): Call<ListObject?>?
    @GET("sso/newssource")
    fun ssoAllSource(): Call<ListObject?>?

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
    //Sign in with SSO
    @POST("sso")
    fun sso(
        @Query("fullname", encoded = true) fullname: String?,
        @Query("email", encoded = true) email: String?,
        @Query("nickname", encoded = true) nickname: String?,
        @Query("avatar", encoded = true) type: String?
    ): Call<SSO?>?
    //Check SSO account in database is exist or not
    @GET("/sso/count")
    fun ssoCount(
        @Query("account", encoded = true) email: String?
    ): Call<CountSSO?>?
    //Update SSO account
    @POST("/sso/update")
    fun updateSSO(
        @Query("user_id", encoded = true) user_id: String?,
        @Query("name") name: String?,
        @Query("avatar") avatar: String?,
    ): Call<UpdateSSO?>?
    //Check nickname is exist or not
    @GET("checknickname")
    fun checkNickname(
        @Query("nickname") nickname: String?,
        @Query("email") email: String?
    ): Call<CheckNickName?>?
    //Verify email
    @POST("verify")
    fun verify(@Query("email", encoded = true) email: String?): Call<Verify?>?
    //Show Recovery Code
    @GET("recoverycode")
    fun recoveryCode(@Query("email", encoded = true) email: String?): Call<Recovery?>?
    //Sign in account with email and password
    @GET("signin")
    fun signIn(
        @Query("account", encoded = true) account: String?,
        @Query("password", encoded = true) password: String?
    ): Call<SignIn?>?
    //Account Settings
    //Update fullname account
    @POST("/account/fullname/update")
    fun updateFullNameAccount(
        @Query("userid", encoded = true) userid: String?,
        @Query("fullname", encoded = true) fullname: String?,
    ): Call<FullName?>?
    //Update username account (or nickname)
    @POST("/account/username/update")
    fun updateUserNameAccount(
        @Query("userid", encoded = true) userid: String?,
        @Query("username", encoded = true) username: String?,
    ): Call<UserName?>?
    //update user birthday
    @POST("/account/birthday/update")
    fun updateBirthdayAccount(
        @Query("userid", encoded = true) userid: String?,
        @Query("birthday", encoded = true) birthday: String?,
    ): Call<Birthday?>?
    //Update sso user birthday
    fun updateBirthdaySSO(
            @Query("userid", encoded = true) userid: String?,
            @Query("birthday", encoded = true) birthday: String?,
    ): Call<Birthday?>?
    //Update user gender
    @POST("/account/gender/update")
    fun updateGenderAccount(
            @Query("userid", encoded = true) userid: String?,
            @Query("gender", encoded = true) gender: String?,
    ): Call<Gender?>?
    //update sso user gender
    @POST("/sso/gender/update")
    fun updateGenderSSO(
            @Query("userid", encoded = true) userid: String?,
            @Query("gender", encoded = true) gender: String?,
    ): Call<Gender?>?
    //Generate new recovery code
    @POST("/generaterecoverycode")
    fun generateRecoveryCode(
        @Query("userid", encoded = true) userid: String?,
    ): Call<RecoveryCode?>?
    //Update password account
    @POST("/account/password/update")
    fun updatePasswordAccount(
        @Query("userid", encoded = true) userid: String?,
        @Query("newpass", encoded = true) newpass: String?,
    ): Call<Password?>?
    //Check old password account before update new password
    @GET("/account/password/check")
    fun checkPasswordAccount(
        @Query("userid", encoded = true) userid: String?,
        @Query("email", encoded = true) email: String?,
        @Query("oldpass", encoded = true) oldpass: String?,
    ): Call<CheckPassword?>?
    //Recovery account with recovery code
    @GET("/account/recovery")
    fun requestRecovery(
            @Query("code", encoded = true) code: String?
    ): Call<SignIn?>?
    //////////////////////////////////////////////////////////////////////////////////
    //Now this is sync part//
    //first is email method: news_source subscribe and news_favourite
    @POST("/account/favourite/add")
    fun accountSourceSubscribe(
        @Query("userid", encoded = true) userid: String?,
        @Query("title", encoded = true) title: String?,
        @Query("sourcename", encoded = true) sourcename: String?,
    ): Call<NewsFavourite?>?
    //services will use params: userid, url, title, imageurl, sourcename
    @POST("/account/favourite/news/add")
    fun accountNewsFavourite(
        @Query("userid", encoded = true) userid: String?,
        @Query("url", encoded = true) url: String?,
        @Query("title", encoded = true) title: String?,
        @Query("imageurl", encoded = true) imageurl: String?,
        @Query("sourcename", encoded = true) sourcename: String?,
    ): Call<SourceSubscribe?>?
    //services will use params: userid, sourceid
    @POST("/account/favourite/delete")
    fun accountSourceUnsubscribe(
        @Query("userid", encoded = true) userid: String?,
        @Query("sourceid", encoded = true) sourceid: String?,
    ): Call<NewsUnfavourite?>?
    //Delete news favourite from user (use params: userid, url, title, imageurl, sourcename)
    @POST("/account/favourite/news/delete")
    fun accountNewsUnfavourite(
        @Query("userid", encoded = true) userid: String?,
        @Query("url", encoded = true) url: String?,
        @Query("title", encoded = true) title: String?,
        @Query("imageurl", encoded = true) imageurl: String?,
        @Query("sourcename", encoded = true) sourcename: String?,
    ): Call<SourceUnsubscribe?>?
    //Get all news favourite from user (use params: userid)
    @GET("/account/favourite/news/show")
    fun accountNewsFavouriteShow(
        @Query("userid", encoded = true) userid: String?,
    ): Call<NewsFavouriteShow?>?
    //Check Source is subscribed or not (use params: userid, sourceid)
    @GET("/account/subscribe/check")
    fun accountSourceSubscribeCheck(
        @Query("userid", encoded = true) userid: String?,
        @Query("sourceid", encoded = true) sourceid: String?,
    ): Call<SourceSubscribeCheck?>?
    ////////////////////////////////////////////////////////////////////////////
    //Second is SSO method, not different with email method
    @POST("/sso/favourite/add")
    fun ssoSourceSubscribe(
            @Query("userid", encoded = true) userid: String?,
            @Query("title", encoded = true) title: String?,
            @Query("sourcename", encoded = true) sourcename: String?,
    ): Call<NewsFavourite?>?
    //services will use params: userid, url, title, imageurl, sourcename
    @POST("/sso/favourite/news/add")
    fun ssoNewsFavourite(
            @Query("userid", encoded = true) userid: String?,
            @Query("url", encoded = true) url: String?,
            @Query("title", encoded = true) title: String?,
            @Query("imageurl", encoded = true) imageurl: String?,
            @Query("sourcename", encoded = true) sourcename: String?,
    ): Call<SourceSubscribe?>?
    //services will use params: userid, sourceid
    @POST("/sso/favourite/delete")
    fun ssoSourceUnsubscribe(
            @Query("userid", encoded = true) userid: String?,
            @Query("sourceid", encoded = true) sourceid: String?,
    ): Call<NewsUnfavourite?>?
    //Delete news favourite from user (use params: userid, url, title, imageurl, sourcename)
    @POST("/sso/favourite/news/delete")
    fun ssoNewsUnfavourite(
            @Query("userid", encoded = true) userid: String?,
            @Query("url", encoded = true) url: String?,
            @Query("title", encoded = true) title: String?,
            @Query("imageurl", encoded = true) imageurl: String?,
            @Query("sourcename", encoded = true) sourcename: String?,
    ): Call<SourceUnsubscribe?>?
    //Get all news favourite from user (use params: userid)
    @GET("/sso/favourite/news/show")
    fun ssoNewsFavouriteShow(
            @Query("userid", encoded = true) userid: String?,
    ): Call<NewsFavouriteShow?>?
    //Check Source is subscribed or not (use params: userid, sourceid)
    @GET("/sso/subscribe/check")
    fun ssoSourceSubscribeCheck(
            @Query("userid", encoded = true) userid: String?,
            @Query("sourceid", encoded = true) sourceid: String?,
    ): Call<SourceSubscribeCheck?>?



}