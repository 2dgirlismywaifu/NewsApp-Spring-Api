package com.notmiyouji.newsapp.java.NewsAPI;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
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

    public void LoadJSONCategory(AppCompatActivity activity, ProgressDialog mDialog, String categoryname, RecyclerView newsViewVertical, String country) {
        Thread loadSourceGeneral = new Thread(() -> {
            callCategory = newsApiInterface.getNewsCategory(country, categoryname, API_KEY);
            assert callCategory != null;
            callCategory.enqueue(new Callback<NewsCategory>() {
                @SuppressLint("NotifyDataSetChanged")
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
                            newsAdapterVertical.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsCategory> call, @NonNull Throwable t) {
                    Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                }
            });

            activity.runOnUiThread(() ->
                    newsViewVertical.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss));
        });
        loadSourceGeneral.start();
    }
}
