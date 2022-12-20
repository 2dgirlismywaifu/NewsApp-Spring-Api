package com.notmiyouji.newsapp.java.RSSURL;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LoadFollowType {

    RecyclerView newsViewVertical;
    LinearLayoutManager newsViewLayoutVertical;
    AppCompatActivity activity;
    ProgressDialog mDialog;

    public LoadFollowType(AppCompatActivity activity, RecyclerView newsViewVertical, ProgressDialog mDialog) {
        this.activity = activity;
        this.newsViewVertical = newsViewVertical;
        this.mDialog = mDialog;
    }

    public void startLoad (String url_type) {
        Thread loadNewsVerticalMode = new Thread(new Runnable() {
            @Override
            public void run() {
                newsViewLayoutVertical = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                FeedMultiRSS feedMultiRSS = new FeedMultiRSS(activity, newsViewVertical, newsViewLayoutVertical);
                feedMultiRSS.MultiFeedVertical(url_type, mDialog);
            }
        });
        loadNewsVerticalMode.start();
    }
}
