package com.notmiyouji.newsapp.java.NewsAPI;

import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.NewsCategory;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("top-headlines")
    Call<News> getLatestNews(

            @Query("country") String country ,
            @Query("apiKey") String apiKey

    );
    @GET("top-headlines")
    Call<NewsCategory> getNewsCategory(
            @Query("country") String country ,
            @Query("category") String category,
            @Query("apiKey") String apiKey

    );
    @GET("everything")
    Call<News> getNewsSearch(

            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey

    );
}
