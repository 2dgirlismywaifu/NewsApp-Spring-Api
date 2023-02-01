package com.notmiyouji.newsapp.java.Category;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.notmiyouji.newsapp.java.RSS2JSON.FeedMultiRSS;

public class RssURLCategory {

    RecyclerView newsViewVertical;
    LinearLayoutManager newsViewLayoutVertical;
    AppCompatActivity activity;
    AlertDialog bar;
    String name;

    public RssURLCategory(AppCompatActivity activity, RecyclerView newsViewVertical, AlertDialog bar, String name) {
        this.activity = activity;
        this.newsViewVertical = newsViewVertical;
        this.bar = bar;
        this.name = name;
    }

    public void startLoad(String url_type) {
        Thread loadNewsVerticalMode = new Thread(() -> {
            newsViewLayoutVertical = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            FeedMultiRSS feedMultiRSS = new FeedMultiRSS(activity, newsViewVertical, newsViewLayoutVertical);
            feedMultiRSS.MultiFeedVertical(url_type, name, bar);
        });
        loadNewsVerticalMode.start();
    }
}
