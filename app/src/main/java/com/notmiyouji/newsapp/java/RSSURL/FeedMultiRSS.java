package com.notmiyouji.newsapp.java.RSSURL;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.RSSFeed.RSSObject;
import com.notmiyouji.newsapp.kotlin.RSSSource.ListObject;
import com.notmiyouji.newsapp.kotlin.RSSSource.NewsDetails;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class FeedMultiRSS {

    static {
        System.loadLibrary("keys");
    }

    public final String RSS2JSON_API_KEY = new String(android.util.Base64.decode(getRSS2JSONAPIKey(), Base64.DEFAULT));
    AppCompatActivity activity;
    LinearLayoutManager linearLayoutManager;
    NewsAPPInterface newsAPPInterface = NewsAppAPI.getAPIClient().create(NewsAPPInterface.class);
    Call<ListObject> call;
    List<NewsDetails> listHorizonal, listVertical;
    NewsDetails newsDetails;
    RecyclerView recyclerView;
    RSSObject rssObject;

    public FeedMultiRSS(AppCompatActivity activity, RecyclerView recyclerView, LinearLayoutManager linearLayoutManager) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.linearLayoutManager = linearLayoutManager;
    }

    public native String getRSS2JSONAPIKey();

    public void MultiFeedHorizontal(String url_type, String name, ProgressDialog progressDialog) {
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
                        try {
                            String getURL = URLEncoder.encode(newsDetails.getUrllink(), "UTF-8");
                            @SuppressLint("NotifyDataSetChanged") Thread startFeed = new Thread(() -> {
                                String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=" + getURL + "&api_key=" + RSS2JSON_API_KEY;
                                HTTPDataHandler http = new HTTPDataHandler();
                                String result = http.GetHTTPData(RSS_to_Json_API);
                                activity.runOnUiThread(() -> {
                                    rssObject = new Gson().fromJson(result, RSSObject.class);
                                    FeedAdapterHorizontal adapter = new FeedAdapterHorizontal(rssObject, activity);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(progressDialog::dismiss);
                                    adapter.notifyDataSetChanged();
                                });
                            });
                            startFeed.start();
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListObject> call, @NonNull Throwable t) {

            }
        });
    }

    public void MultiFeedVertical(String url_type, String name, ProgressDialog progressDialog) {
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
                        try {
                            if (Objects.equals(newsDetails.getUrllink(), "not_available")) {
                                progressDialog.dismiss();
                                Toast.makeText(activity, activity.getString(R.string.no_topic_available) + url_type, Toast.LENGTH_SHORT).show();
                            } else {
                                String getURL = URLEncoder.encode(newsDetails.getUrllink(), "UTF-8");
                                @SuppressLint("NotifyDataSetChanged") Thread startFeed = new Thread(() -> {
                                    String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=" + getURL + "&api_key=" + RSS2JSON_API_KEY;
                                    HTTPDataHandler http = new HTTPDataHandler();
                                    String result = http.GetHTTPData(RSS_to_Json_API);
                                    activity.runOnUiThread(() -> {
                                        rssObject = new Gson().fromJson(result, RSSObject.class);
                                        FeedAdapterVertical adapter = new FeedAdapterVertical(rssObject, activity);
                                        recyclerView.setLayoutManager(linearLayoutManager);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(progressDialog::dismiss);
                                        adapter.notifyDataSetChanged();
                                    });
                                });
                                startFeed.start();
                            }

                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
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
}








