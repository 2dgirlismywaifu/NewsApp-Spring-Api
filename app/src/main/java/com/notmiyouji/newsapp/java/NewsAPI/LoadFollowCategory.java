package com.notmiyouji.newsapp.java.NewsAPI;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.java.global.recycleviewadapter.newsapi.NewsAdapterVertical;
import com.notmiyouji.newsapp.kotlin.NewsAPIInterface;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.ArticleCategory;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.NewsCategory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadFollowCategory {
    public static final String API_KEY = new NewsAPIKey().getNEWSAPIKEY(); //the newsAPI key is here
    List<ArticleCategory> articles2 = new ArrayList<>(); //include category
    NewsAdapterVertical newsAdapterVertical;
    NewsAPIInterface newsApiInterface = NewsAPIKey.getAPIClient().create(NewsAPIInterface.class);
    Call<NewsCategory> callCategory;

    public void LoadJSONCategory(AppCompatActivity activity, ProgressDialog mDialog, String categoryname, RecyclerView newsViewVertical) {
        Thread loadSourceGeneral = new Thread(() -> {
            callCategory = newsApiInterface.getNewsCategory("us", categoryname, API_KEY);
            assert callCategory != null;
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
                public void onFailure(@NonNull Call<NewsCategory> call, Throwable t) {
                }
            });

            activity.runOnUiThread(() ->
                    newsViewVertical.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss));
        });
        loadSourceGeneral.start();
    }
}
