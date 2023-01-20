package com.notmiyouji.newsapp.java.NewsDetails;

import android.app.Activity;
import android.content.Intent;

import com.notmiyouji.newsapp.kotlin.SharedSettings.UseChromeShared;

public class OpenNewsDetails {
    private final String url;
    private final String title;
    private final String img;
    private final String source;
    private final String pubdate;
    private final Activity activity;

    public OpenNewsDetails(String url, String title, String img, String source, String pubdate, Activity activity) {
        this.url = url;
        this.title = title;
        this.img = img;
        this.source = source;
        this.pubdate = pubdate;
        this.activity = activity;
    }

    public void openNewsDetails() {
        if (new UseChromeShared(activity).getEnableChrome()) {
            //Open in chrome custom tabs
            NewsDetailsChrome chromeClient = new NewsDetailsChrome(
                    this.url,
                    this.title,
                    this.img,
                    this.url,
                    this.pubdate, activity);
            chromeClient.openNewsDetails();
        } else {
            //No chrome installed, open in webview
            Intent intent = new Intent(activity, NewsDetailWebView.class);
            intent.putExtra("url", this.url);
            intent.putExtra("title", this.title);
            intent.putExtra("img", this.img);
            intent.putExtra("source", this.source);
            intent.putExtra("pubdate", this.pubdate);
            activity.startActivity(intent);
        }
    }
}
