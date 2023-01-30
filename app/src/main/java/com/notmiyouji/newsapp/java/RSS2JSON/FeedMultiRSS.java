package com.notmiyouji.newsapp.java.RSS2JSON;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    public void MultiFeedHorizontal(String url_type, String name, ProgressBar bar) {
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
                        RSS2JSONHorizonal(getURL, name, bar);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListObject> call, @NonNull Throwable t) {

            }
        });
    }

    public void MultiFeedVertical(String url_type, String name, ProgressBar bar) {
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
                            bar.setVisibility(View.GONE);
                            Toast.makeText(activity, activity.getString(R.string.no_topic_available) + url_type, Toast.LENGTH_SHORT).show();
                        } else {
                            //String getURL = URLEncoder.encode(newsDetails.getUrllink(), "UTF-8");
                            String getURL = newsDetails.getUrllink();
                            RSS2JSONVertical(getURL, name, bar);
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

    public void RSS2JSONVertical(String url, String name, ProgressBar bar) {
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
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(adapterVertical);
                            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                                if (adapterVertical.getItemCount() > 0) {
                                    bar.setVisibility(View.GONE);
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


    public void RSS2JSONHorizonal(String url, String name, ProgressBar bar) {
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
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(adapterHorizontal);
                            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                                if (adapterHorizontal.getItemCount() > 0) {
                                    bar.setVisibility(View.GONE);
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








