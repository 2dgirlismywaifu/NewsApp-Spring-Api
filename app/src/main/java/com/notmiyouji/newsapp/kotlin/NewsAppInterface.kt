/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notmiyouji.newsapp.kotlin

import com.notmiyouji.newsapp.kotlin.model.NewsAppResult
import com.notmiyouji.newsapp.kotlin.model.NewsDetails
import com.notmiyouji.newsapp.kotlin.model.NewsFavourite
import com.notmiyouji.newsapp.kotlin.model.RecoveryAccount
import com.notmiyouji.newsapp.kotlin.model.RecoveryCode
import com.notmiyouji.newsapp.kotlin.model.SignIn
import com.notmiyouji.newsapp.kotlin.model.SignUp
import com.notmiyouji.newsapp.kotlin.model.SourceSubscribe
import com.notmiyouji.newsapp.kotlin.model.UserInformation
import com.notmiyouji.newsapp.kotlin.model.VerifyEmail
import com.notmiyouji.newsapp.kotlin.model.VerifyNickNameEmail
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NewsAppInterface {

    // <editor-fold default-state="collapsed" desc="This is a part of NewsApp-NewsSource">//GEN-BEGIN:initComponents
    /**
     * Guest user: Get all list news source for guest, limit 3 news source
     * @return NewsAppResult
     */
    @GET("news-app/guest/news-source")
    fun guestAllSource(): Call<NewsAppResult?>?

    /**
     * Guest user: Get news details source for guest user
     * @deprecated This method is deprecated cause it's not used anymore
     * @param type: News Type
     * @param name: News Name
     * @return NewsDetails
     */
    @GET("news-app/guest/news-details")
    @Deprecated("This method is deprecated cause it's not used anymore")
    fun guestNewsDetails(
        @Query("type", encoded = true) type: String?,
        @Query("name", encoded = true) name: String?
    ): Call<NewsDetails?>?

    /**
     * Only for user login: Get all list news source for user login, no limit news source
     * @param userid: User Id
     * @return NewsAppResult
     *
     */
    @GET("news-app/account/news-source")
    fun accountAllSource(
        @Query("userid", encoded = true) userid: String?
    ): Call<NewsAppResult?>?

    /**
     * Only for user login: Check Source is Subscribed or not and show it with button Unsubscribe
     * @param userId: User Id
     * @param sourceId: Source Id
     * @return SourceSubscribeVerify
     *
     */
    @GET("news-app/account/check-subscribe")
    fun accountCheckSourceSubscribe(
        @Query("userId", encoded = true) userId: String?,
        @Query("sourceId", encoded = true) sourceId: String?
    ): Call<SourceSubscribe?>?

    /**
     * Only for user login: Check News is Favourite or not and show it with button Favourite
     * @param userId: User Id
     * @param title: News Title
     * @return NewsFavourite
     */
    @GET("news-app/account/check-news-favourite")
    fun accountCheckNewsFavourite(
        @Query("userId", encoded = true) userId: String?,
        @Query("title", encoded = true) title: String?
    ): Call<NewsFavourite?>?

    /**
     * Only for user login: Subscribe News Source
     * @param userId: User Id
     * @param sourceId: Source Id
     * @return SourceSubscribe
     *
     */
    @POST("news-app/account/subscribe-source")
    fun accountSubscribeNewsSource(
        @Query("userId", encoded = true) userId: String?,
        @Query("sourceId", encoded = true) sourceId: String?
    ): Call<SourceSubscribe?>?

    /**
     * Only for user login: Unsubscribe News Source
     * @param userId: User Id
     * @param sourceId: Source Id
     * @return SourceSubscribe
     */
    @DELETE("news-app/account/unsubscribe-source")
    fun unSubscribeNewsSource(
        @Query("userId", encoded = true) userId: String?,
        @Query("sourceId", encoded = true) sourceId: String?
    ): Call<SourceSubscribe?>?

    /**
     * Only for user login: Show news favourite saved by user
     * @param userId: User Id
     * @return NewsAppResult
     */
    @GET("news-app/account/show-news-favourite")
    fun showNewsFavouriteByUserId(
        @Query("userId") userId: String?
    ): Call<NewsAppResult?>?

    /**
     * Only for user login: Save the news favourite by user
     * @param userId: User Id
     * @param url: News Url
     * @param title: News Title
     * @param imageUrl: News Image Url
     * @param pubDate: News Publish Date
     * @return NewsFavourite
     */
    @POST("news-app/account/save-news-favourite")
    fun saveNewsFavouriteByUser(
        @Query("userId") userId: String?,
        @Query("url") url: String?,
        @Query("title", encoded = true) title: String?,
        @Query("imageUrl") imageUrl: String?,
        @Query("pubDate") pubDate: String?
    ): Call<NewsFavourite?>?

    /**
     * Only for user login: Delete the news favourite by user
     * @param userId: User Id
     * @param favouriteId: News Favourite Id
     * @param title: News Title
     * @return NewsFavourite
     */
    @DELETE("news-app/account/delete-news-favourite")
    fun deleteNewsFavouriteByUser(
        @Query("userId", encoded = true) userId: String?,
        @Query("favouriteId", encoded = true) favouriteId: String?,
        @Query("title", encoded = true) title: String?
    ): Call<NewsFavourite?>?

    /**
     * Only for user login: Get list of url follow source, this method is deprecated cause it's not used anymore
     * @deprecated This method is deprecated cause it's not used anymore
     * @param name: News Name
     * @return NewsAppResult
     */
    @GET("news-app/account/news-details/list-url")
    @Deprecated("This method is deprecated cause it's not used anymore")
    fun getListUrlForEachSource(
        @Query("name", encoded = true) name: String?
    ): Call<NewsAppResult?>?

    /**
     * Only for user login: Get list of url for each sources, it will display in News Details screen
     * @param name: News Name
     * @return NewsAppResult
     */
    @GET("news-app/account/news-details/list-rss")
    fun getRssListFollowSource(
        @Query("name", encoded = true) name: String?
    ): Call<NewsAppResult?>?
    // </editor-fold>//GEN-END:initComponents

    // <editor-fold default-state="collapsed" desc="This is a part of NewsApp-NewsApi">//GEN-BEGIN:initComponents

    /**
     * Get list of country supported by NewsApi
     * @return NewsAppResult
     */
    @GET("news-api/country/list")
    fun getNewsApiCountrySupport(): Call<NewsAppResult>?

    /**
     * Get the country code to use in newsApi
     * @param country: Country name
     * @return NewsAppResult
     */
    @GET("news-api/country/code")
    fun getNewsApiCountryCode(
        @Query("name", encoded = true) country: String?
    ): Call<NewsAppResult>?

    /**
     * Get top headlines news from NewsApi
     * @param keyWord: Key word to search (not required)
     * @param country: Country code
     * @param category: Category of news (not required)
     * @param size: Size of news
     * @return NewsAppResult
     */
    @GET("news-api/top-headlines")
    fun getNewsTopHeadlinesFromNewsApi(
        @Query("keyWord", encoded = true) keyWord: String?,
        @Query("country") country: String?,
        @Query("category", encoded = true) category: String?,
        @Query("size", encoded = true) size: String?
    ): Call<NewsAppResult>?

    /**
     * Get every things news by KeyWord to find from NewsApi
     * @param keyWord: Key word to search
     * @param sortBy: Sort by relevancy, popularity, publishedAt (not required)
     * @param size: Size of news
     * @return NewsAppResult
     */
    @GET("news-api/everything")
    fun getEveryThingsNewsFromNewsApi(
        @Query("keyWord", encoded = true) keyWord: String?,
        @Query("sortBy", encoded = true) sortBy: String?,
        @Query("size", encoded = true) size: String?,
    ): Call<NewsAppResult>?

    // </editor-fold>//GEN-END:initComponents

    // <editor-fold default-state="collapsed" desc="This is a part of NewsApp-Rss2Json">//GEN-BEGIN:initComponents

    /**
     * Convert Rss Url to Json
     * @param userId: User Id (not required, can be empty if user is guest)
     * @param type: News Type
     * @param size: limit size each source
     * @return NewsAppResult
     */
    @GET("news-app/rss-to-json")
    fun convertRssUrl2Json(
        @Query("userId", encoded = true) userId: String?,
        @Query("type", encoded = true) type: String?,
        @Query("size", encoded = true) size: String?
    ): Call<NewsAppResult?>?

    /**
     * Search news from Rss with key word
     * @param userId: User Id (not required, can be empty if user is guest)
     * @param keyWord: News Type
     * @param size: limit size each source
     * @return NewsAppResult
     */
    @GET("news-app/search-news")
    fun searchNewsFromRss(
        @Query("userId", encoded = true) userId: String?,
        @Query("keyWord", encoded = true) keyWord: String?,
        @Query("size", encoded = true) size: String?
    ): Call<NewsAppResult?>?

    // </editor-fold>//GEN-END:initComponents

    // <editor-fold default-state="collapsed" desc="This is a part of NewsApp-User-Controller">//GEN-BEGIN:initComponents

    /**
     * Sign in with email and userToken
     * @param email: Can be the email or nickname
     * @param userToken: userToken Login (it will be verified by server)
     * @return SignIn
     */
    @GET("news-app/user/sign-in")
    fun signIn(
        @Query("email", encoded = true) email: String?,
        @Query("userToken", encoded = true) userToken: String?
    ): Call<SignIn?>?

    /**
     * Sign in with google account
     * @param fullName: Full name of user
     * @param email: Email of user
     * @param userToken: User token of user
     * @param nickName: Nick name of user
     * @param avatar: Avatar of user
     * @return SignIn
     */
    @GET("news-app/user/sign-in-by-google")
    fun signInWithGoogle(
        @Query("fullName", encoded = true) fullName: String?,
        @Query("email", encoded = true) email: String?,
        @Query("userToken", encoded = true) userToken: String?,
        @Query("nickName", encoded = true) nickName: String?,
        @Query("avatar", encoded = true) avatar: String?
    ): Call<SignIn?>?

    /**
     * Sign up an account
     * @param fullName: Full name of user
     * @param email: Email of user
     * @param userToken: User token of user
     * @param nickName: Nick name of user
     * @return SignIn
     */
    @POST("news-app/user/register")
    fun signUpAnAccount(
        @Query("fullName", encoded = true) fullName: String?,
        @Query("email", encoded = true) email: String?,
        @Query("userToken", encoded = true) userToken: String?,
        @Query("nickname", encoded = true) nickName: String?
    ): Call<SignUp?>?

    /**
     * Verify nickName (make sure nickName is unique)
     * @param nickname: Nick name of user
     * @param email: Email of user
     * @return VerifyNickName
     */
    @GET("news-app/user/verify-nickname-email")
    fun verifyNickNameAndEmail(
        @Query("nickname", encoded = true) nickname: String?,
        @Query("email", encoded = true) email: String?
    ): Call<VerifyNickNameEmail?>?

    /**
     * Verify email (make sure email is unique)
     * @param email: Email of user
     * @return VerifyEmail
     */
    @POST("news-app/user/verify-email")
    fun verifyEmail(
        @Query("email", encoded = true) email: String?
    ): Call<VerifyEmail?>?

    /**
     * Get recovery code
     * @param email: Email of user
     * @return Recovery
     */
    @GET("news-app/user/recovery-code")
    fun getRecoveryCode(
        @Query("email", encoded = true) email: String?
    ): Call<RecoveryCode?>?

    /**
     * Create new recovery code
     * @param userId: User Id
     * @return Recovery
     */
    @POST("news-app/user/generate-recovery")
    fun createNewRecoveryCode(
        @Query("userId", encoded = true) userId: String?
    ): Call<RecoveryCode?>?

    /**
     * Verify recovery code for change password
     * @param code: Recovery Code
     * @return RecoveryAccount
     */
    @GET("news-app/user/verify-recovery-code")
    fun recoveryAccountByRecoveryCode(
        @Query("code", encoded = true) code: String?,
    ): Call<RecoveryAccount?>?

    /**
     * Update user information
     * @param userId: User Id
     * @param userName: User Name
     * @param fullName: Full Name
     * @param birthday: Birthday
     * @param gender: Gender
     * @param avatar: Avatar
     * @return UserInformation
     */
    @POST("news-app/user/update")
    fun updateUserInformation(
        @Query("userid", encoded = true) userId: String?,
        @Query("userName", encoded = true) userName: String?,
        @Query("fullName", encoded = true) fullName: String?,
        @Query("birthday", encoded = true) birthday: String?,
        @Query("gender", encoded = true) gender: String?,
        @Query("avatar", encoded = true) avatar: String?,
    ): Call<UserInformation?>?

    /**
     * Change user password
     * @param userId: User Id
     * @param email: Email
     * @param newToken: New Password
     * @return UserInformation
     */
    @POST("news-app/user/change-user-token")
    fun changeUserToken(
        @Query("userId", encoded = true) userId: String?,
        @Query("email", encoded = true) email: String?,
        @Query("newToken", encoded = true) newToken: String?,
    ): Call<UserInformation?>?

    // </editor-fold>//GEN-END:initComponents

}