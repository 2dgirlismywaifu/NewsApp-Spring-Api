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

package com.notmiyouji.newsapp.java.RSS2JSON;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.java.Retrofit.Rss2jsonAPI;
import com.notmiyouji.newsapp.kotlin.RSSFeed.Category.CategoryType;
import com.notmiyouji.newsapp.kotlin.RSSFeed.Category.ItemsCategory;
import com.notmiyouji.newsapp.kotlin.RSSFeed.Items;
import com.notmiyouji.newsapp.kotlin.RSSFeed.RSS2JSON;
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject;
import com.notmiyouji.newsapp.kotlin.RSSSource.NewsDetails;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.RSS2JSONInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class FeedMultiRSS {

    static {
        System.loadLibrary("keys");
    }

    public final String RSS2JSON_API_KEY = new String(android.util.Base64.decode(getRSS2JSONAPIKey(), Base64.DEFAULT));
    AppCompatActivity activity;
    LinearLayoutManager linearLayoutManager;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);
    RSS2JSONInterface rss2JSONInterface = Rss2jsonAPI.getAPIClient().create(RSS2JSONInterface.class);
    Call<ListObject> call;
    Call<RSS2JSON> callRSS;
    Call<CategoryType> callVertical;
    List<NewsDetails> listHorizonal, listVertical;
    List<Items> items = new ArrayList<>();
    List<ItemsCategory> itemsCategories = new ArrayList<>();
    NewsDetails newsDetails;
    RecyclerView recyclerView;
    FeedAdapterHorizontal adapterHorizontal;
    FeedAdapterVertical adapterVertical;

    public FeedMultiRSS(AppCompatActivity activity, RecyclerView recyclerView, LinearLayoutManager linearLayoutManager) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.linearLayoutManager = linearLayoutManager;
    }

    public native String getRSS2JSONAPIKey();

    public void MultiFeedHorizontal(String url_type, String name, AlertDialog alertDialog) {
        call = newsAPPInterface.getAllNewsDetails(url_type, name);
        assert call != null;
        listHorizonal = new ArrayList<>();
        call.enqueue(new retrofit2.Callback<ListObject>() {
            @Override
            public void onResponse(@NonNull Call<ListObject> call, @NonNull retrofit2.Response<ListObject> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getNewsDetails() != null) {
                        if (!listHorizonal.isEmpty()) {
                            listHorizonal.clear();
                        }
                        listHorizonal = response.body().getNewsDetails();
                        newsDetails = listHorizonal.get(0);
                        //String getURL = URLEncoder.encode(newsDetails.getUrllink(), "UTF-8");
                        String getURL = newsDetails.getUrllink();
                        //first clear adapter
                        clearAdapterHorizontal(name);
                        RSS2JSONHorizonal(getURL, name, alertDialog);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListObject> call, @NonNull Throwable t) {

            }
        });
    }

    public void MultiFeedVertical(String url_type, String name, AlertDialog alertDialog) {
        call = newsAPPInterface.getAllNewsDetails(url_type, name);
        assert call != null;
        listVertical = new ArrayList<>();
        call.enqueue(new retrofit2.Callback<ListObject>() {
            @Override
            public void onResponse(@NonNull Call<ListObject> call, @NonNull retrofit2.Response<ListObject> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getNewsDetails() != null) {
                        if (!listVertical.isEmpty()) {
                            listVertical.clear();
                        }
                        listVertical = response.body().getNewsDetails();
                        newsDetails = listVertical.get(0);
                        if (Objects.equals(newsDetails.getUrllink(), "not_available")) {
                            alertDialog.dismiss();
                            Toast.makeText(activity, activity.getString(R.string.no_topic_available) + url_type, Toast.LENGTH_SHORT).show();
                        } else {
                            //String getURL = URLEncoder.encode(newsDetails.getUrllink(), "UTF-8");
                            String getURL = newsDetails.getUrllink();
                            RSS2JSONVertical(getURL, name, alertDialog);
                            clearAdapterVertical(name);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListObject> call, @NonNull Throwable t) {
                System.out.println("Failure");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearAdapterVertical(String name) {
        adapterVertical = new FeedAdapterVertical(itemsCategories, activity, name);
        adapterVertical.clear();
        adapterVertical.notifyDataSetChanged();
    }

    public void RSS2JSONVertical(String url, String name, AlertDialog alertDialog) {
        callVertical = rss2JSONInterface.getCategoryType(url, RSS2JSON_API_KEY);
        assert callVertical != null;
        callVertical.enqueue(new retrofit2.Callback<CategoryType>() {
            @Override
            public void onResponse(@NonNull Call<CategoryType> call, @NonNull Response<CategoryType> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getItemsCategory() != null) {
                        if (!itemsCategories.isEmpty()) {
                            itemsCategories.clear();
                        }
                        itemsCategories = response.body().getItemsCategory();
                        @SuppressLint("NotifyDataSetChanged")
                        Thread startFeed = new Thread(() -> activity.runOnUiThread(() -> {
                            adapterVertical = new FeedAdapterVertical(itemsCategories, activity, name);
                            recyclerView.removeAllViews();
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(adapterVertical);
                            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                                if (adapterVertical.getItemCount() > 0) {
                                    alertDialog.dismiss();
                                }
                            });
                            adapterVertical.notifyDataSetChanged();
                        }));
                        startFeed.start();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryType> call, @NonNull Throwable t) {
                System.out.println("Failure");
            }
        });
    }

    public void filterVertical(String text) {
        //This function not working cause data always be null
        List<ItemsCategory> newsFilter = new ArrayList<>();
        for (ItemsCategory itemsCategory : itemsCategories) {
            if (itemsCategory.getTitle().toLowerCase().contains(text.toLowerCase())) {
                newsFilter.add(itemsCategory);
            }
        }
        if (newsFilter.size() == 0) {
            Toast.makeText(activity, activity.getString(R.string.no_result_found), Toast.LENGTH_SHORT).show();
        } else {
            adapterVertical.filterList(newsFilter);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearAdapterHorizontal(String name) {
        adapterHorizontal = new FeedAdapterHorizontal(items, activity, name);
        adapterHorizontal.clear();
        adapterHorizontal.notifyDataSetChanged();
    }

    public void RSS2JSONHorizonal(String url, String name, AlertDialog bar) {
        callRSS = rss2JSONInterface.getRSS2JSON(url, RSS2JSON_API_KEY);
        assert callRSS != null;
        callRSS.enqueue(new retrofit2.Callback<RSS2JSON>() {
            @Override
            public void onResponse(@NonNull Call<RSS2JSON> call, @NonNull Response<RSS2JSON> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getItems() != null) {
                        if (!items.isEmpty()) {
                            items.clear();
                        }
                        items = response.body().getItems();
                        @SuppressLint("NotifyDataSetChanged")
                        Thread startFeed = new Thread(() -> activity.runOnUiThread(() -> {
                            adapterHorizontal = new FeedAdapterHorizontal(items, activity, name);
                            recyclerView.removeAllViews();
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(adapterHorizontal);
                            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                                if (adapterHorizontal.getItemCount() > 0) {
                                    bar.dismiss();
                                }
                            });
                            adapterHorizontal.notifyDataSetChanged();
                        }));
                        startFeed.start();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RSS2JSON> call, @NonNull Throwable t) {
                System.out.println("Failure");
            }
        });
    }

    public void filterHorizonal(String text) {
        List<Items> filterHorizonal = new ArrayList<>();
        for (Items items : items) {
            if (Objects.requireNonNull(items.getTitle()).toLowerCase().contains(text.toLowerCase())) {
                filterHorizonal.add(items);
            }
        }
        if (filterHorizonal.size() == 0) {
            Toast.makeText(activity, activity.getString(R.string.no_result_found), Toast.LENGTH_SHORT).show();
        } else {
            adapterHorizontal.filterList(filterHorizonal);
        }
    }
}








