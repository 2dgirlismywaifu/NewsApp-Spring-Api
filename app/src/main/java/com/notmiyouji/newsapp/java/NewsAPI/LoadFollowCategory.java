package com.notmiyouji.newsapp.java.NewsAPI;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.java.NewsAPI.NewsAPIModels.Category.ArticleCategory;
import com.notmiyouji.newsapp.java.NewsAPI.NewsAPIModels.Category.NewsCategory;
import com.notmiyouji.newsapp.java.global.recycleviewadapter.newsapi.NewsAdapterVertical;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadFollowCategory {
    public static final String API_KEY = new NewsAPIKey().getNEWSAPIKEY(); //the newsAPI key is here
    List<ArticleCategory> articles2 = new ArrayList<>(); //include category
    NewsAdapterVertical newsAdapterVertical;
    APIInterface apiInterface = NewsAPIKey.getAPIClient().create(APIInterface.class);
    Call<NewsCategory> callCategory;

    public void LoadJSONCategory(AppCompatActivity activity, ProgressDialog mDialog, String categoryname, RecyclerView newsViewVertical) {
        Thread loadSourceGeneral = new Thread(() -> {
            callCategory = apiInterface.getNewsCategory("us", categoryname, API_KEY);
            callCategory.enqueue(new Callback<NewsCategory>() {
                @Override
                public void onResponse(@NonNull Call<NewsCategory> callCategory, @NonNull Response<NewsCategory> response2) {
                    if (response2.isSuccessful()) {
                        assert response2.body() != null;
                        if (response2.body().getArticle() != null) {
                            if (!articles2.isEmpty()) {
                                articles2.clear();
                            }
                            articles2 = response2.body().getArticle();
                            LinearLayoutManager newsAPIVerticalLayout = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                            newsViewVertical.setLayoutManager(newsAPIVerticalLayout);
                            newsAdapterVertical = new NewsAdapterVertical(articles2, activity);
                            newsViewVertical.setAdapter(newsAdapterVertical);

                        }
                    }
                }

                @Override
                public void onFailure(Call<NewsCategory> call, Throwable t) {
                }
            });

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newsViewVertical.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss);
                }
            });
        });
        loadSourceGeneral.start();
    }
}
