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

package com.notmiyouji.newsapp.java.category;

import android.annotation.SuppressLint;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.newsapi.NewsAdapterVertical;
import com.notmiyouji.newsapp.java.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.model.NewsAppResult;
import com.notmiyouji.newsapp.kotlin.newsapi.Article;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsApiCategory {
    private final NewsAppInterface newsAppInterface = NewsAppApi.getAPIClient().create(NewsAppInterface.class);
    private List<Article> articles = new ArrayList<>(); //include category
    private NewsAdapterVertical newsAdapterVertical;
    private Call<NewsAppResult> callCategory;

    public void searchEveryThingByKeyWord(AppCompatActivity activity, AlertDialog mDialog, RecyclerView newsViewVertical, String keyWord) {
        Thread findEveryThing = new Thread(() -> {
            callCategory = newsAppInterface.getEveryThingsNewsFromNewsApi(keyWord, "popularity" ,"5");
            assert callCategory != null;
            callCategory.enqueue(new Callback<>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<NewsAppResult> callCategory, @NonNull Response<NewsAppResult> response2) {
                    if (response2.isSuccessful()) {
                        assert response2.body() != null;
                        if (response2.body().getArticle() != null) {
                            if (!articles.isEmpty()) {
                                articles.clear();
                            }
                            articles = response2.body().getArticle();
                            LinearLayoutManager newsAPIVerticalLayout = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                            newsViewVertical.setLayoutManager(newsAPIVerticalLayout);
                            newsAdapterVertical = new NewsAdapterVertical(articles, activity);
                            newsViewVertical.setAdapter(newsAdapterVertical);
                            newsAdapterVertical.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                    Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                }
            });

            activity.runOnUiThread(() ->
                    newsViewVertical.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss));
        });
        findEveryThing.start();
    }
    public void LoadJSONCategory(AppCompatActivity activity, AlertDialog mDialog, String categoryName, RecyclerView newsViewVertical, String country, String keyWord) {
        Thread loadSourceGeneral = new Thread(() -> {
            callCategory = newsAppInterface.getNewsTopHeadlinesFromNewsApi(keyWord, country, categoryName, "20");
            assert callCategory != null;
            callCategory.enqueue(new Callback<>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<NewsAppResult> callCategory, @NonNull Response<NewsAppResult> response2) {
                    if (response2.isSuccessful()) {
                        assert response2.body() != null;
                        if (response2.body().getArticle() != null) {
                            if (!articles.isEmpty()) {
                                articles.clear();
                            }
                            articles = response2.body().getArticle();
                            LinearLayoutManager newsAPIVerticalLayout = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                            newsViewVertical.setLayoutManager(newsAPIVerticalLayout);
                            newsAdapterVertical = new NewsAdapterVertical(articles, activity);
                            newsViewVertical.setAdapter(newsAdapterVertical);
                            newsAdapterVertical.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                    Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                }
            });

            activity.runOnUiThread(() ->
                    newsViewVertical.getViewTreeObserver().addOnGlobalLayoutListener(mDialog::dismiss));
        });
        loadSourceGeneral.start();
    }
}
