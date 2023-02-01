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
    AlertDialog alertDialog;
    String name;

    public RssURLCategory(AppCompatActivity activity, RecyclerView newsViewVertical, AlertDialog alertDialog, String name) {
        this.activity = activity;
        this.newsViewVertical = newsViewVertical;
        this.alertDialog = alertDialog;
        this.name = name;
    }

    public void startLoad(String url_type) {
        Thread loadNewsVerticalMode = new Thread(() -> {
            newsViewLayoutVertical = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            FeedMultiRSS feedMultiRSS = new FeedMultiRSS(activity, newsViewVertical, newsViewLayoutVertical);
            feedMultiRSS.MultiFeedVertical(url_type, name, alertDialog);
        });
        loadNewsVerticalMode.start();
    }
}
