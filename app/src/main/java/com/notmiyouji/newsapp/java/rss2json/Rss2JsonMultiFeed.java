package com.notmiyouji.newsapp.java.rss2json;

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

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.java.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.Utils;
import com.notmiyouji.newsapp.kotlin.model.NewsAppResult;
import com.notmiyouji.newsapp.kotlin.model.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class Rss2JsonMultiFeed {

    AppCompatActivity activity;
    LinearLayoutManager linearLayoutManager;
    NewsAppInterface newsAppInterface = NewsAppApi.getAPIClient().create(NewsAppInterface.class);
    Call<NewsAppResult> call;
    List<Result> items = new ArrayList<>();
    RecyclerView recyclerView;
    Rss2JsonAdapterHorizontal adapterHorizontal;
    Rss2JsonAdapterVertical adapterVertical;

    public Rss2JsonMultiFeed(AppCompatActivity activity, RecyclerView recyclerView, LinearLayoutManager linearLayoutManager) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.linearLayoutManager = linearLayoutManager;
    }

    public void rss2JsonVertical(String userId, String type, AlertDialog alertDialog) {
        call = newsAppInterface.convertRssUrl2Json(userId, Utils.encodeToBase64(type), "10");
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<NewsAppResult> call, @NonNull Response<NewsAppResult> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getResult() != null) {
                        if (!items.isEmpty()) {
                            items.clear();
                        }
                        items = response.body().getResult();
                        adapterVertical = new Rss2JsonAdapterVertical(items, activity);
                        recyclerView.removeAllViews();
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapterVertical);
                        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                            if (adapterVertical.getItemCount() > 0) {
                                alertDialog.dismiss();
                            }
                        });
                        adapterVertical.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                System.out.println("Failure");
            }
        });
    }

    public void rss2JsonVerticalByKeyWord(String userId, String keyword, AlertDialog alertDialog) {
        call = newsAppInterface.searchNewsFromRss(userId, Utils.encodeToBase64(keyword), "5");
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<NewsAppResult> call, @NonNull Response<NewsAppResult> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getResult() != null) {
                        if (!items.isEmpty()) {
                            items.clear();
                        }
                        items = response.body().getResult();
                        adapterVertical = new Rss2JsonAdapterVertical(items, activity);
                        recyclerView.removeAllViews();
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapterVertical);
                        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                            if (adapterVertical.getItemCount() > 0) {
                                alertDialog.dismiss();
                            }
                        });
                        adapterVertical.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                System.out.println("Failure");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearAdapterVertical() {
        adapterVertical = new Rss2JsonAdapterVertical(items, activity);
        adapterVertical.clear();
        adapterVertical.notifyDataSetChanged();
    }


    public void rss2JsonHorizontal(String userId, String type, AlertDialog alertDialog) {
        call = newsAppInterface.convertRssUrl2Json(userId, Utils.encodeToBase64(type), "5");
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<NewsAppResult> call, @NonNull Response<NewsAppResult> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getResult() != null) {
                        if (!items.isEmpty()) {
                            items.clear();
                        }
                        items = response.body().getResult();
                        adapterHorizontal = new Rss2JsonAdapterHorizontal(items, activity);
                        recyclerView.removeAllViews();
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapterHorizontal);
                        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                            if (adapterHorizontal.getItemCount() > 0) {
                                alertDialog.dismiss();
                            }
                        });
                        adapterHorizontal.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsAppResult> call, @NonNull Throwable t) {
                System.out.println("Failure");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearAdapterHorizontal() {
        adapterHorizontal = new Rss2JsonAdapterHorizontal(items, activity);
        adapterHorizontal.clear();
        adapterHorizontal.notifyDataSetChanged();
    }
}
