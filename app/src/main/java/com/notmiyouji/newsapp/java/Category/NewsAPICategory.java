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

package com.notmiyouji.newsapp.java.Category;

import android.annotation.SuppressLint;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.NewsAPI.NewsAdapterVertical;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPIKey;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.ArticleCategory;
import com.notmiyouji.newsapp.kotlin.NewsAPIModels.Category.NewsCategory;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPIInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAPICategory {
    public static final String API_KEY = new NewsAPIKey().getNEWSAPIKEY(); //the newsAPI key is here
    List<ArticleCategory> articleCategories = new ArrayList<>(); //include category
    NewsAdapterVertical newsAdapterVertical;
    NewsAPIInterface newsApiInterface = NewsAPIKey.getAPIClient().create(NewsAPIInterface.class);
    Call<NewsCategory> callCategory;

    public void LoadJSONCategory(AppCompatActivity activity, AlertDialog mDialog, String categoryname, RecyclerView newsViewVertical, String country) {
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
                            if (!articleCategories.isEmpty()) {
                                articleCategories.clear();
                            }
                            articleCategories = response2.body().getArticle();
                            LinearLayoutManager newsAPIVerticalLayout = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                            newsViewVertical.setLayoutManager(newsAPIVerticalLayout);
                            newsAdapterVertical = new NewsAdapterVertical(articleCategories, activity);
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

    //Doesn't work if choose another category :(
    public void filterVertical(String text) {
        List<ArticleCategory> listVertical = new ArrayList<>();
        for (ArticleCategory item : articleCategories) {
            if (Objects.requireNonNull(item.getTitle()).toLowerCase().contains(text.toLowerCase())) {
                listVertical.add(item);
            }
        }
        newsAdapterVertical.filterList(listVertical);
    }
}
