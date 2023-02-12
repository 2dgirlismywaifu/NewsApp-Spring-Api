package com.notmiyouji.newsapp.kotlin.OpenActivity

import android.app.Activity
import android.content.Intent
import com.notmiyouji.newsapp.java.NewsDetails.NewsDetailWebView
import com.notmiyouji.newsapp.java.NewsDetails.NewsDetailsChrome
import com.notmiyouji.newsapp.kotlin.SharedSettings.UseChromeShared

class OpenNewsDetails(
    private val url: String,
    private val title: String,
    private val img: String,
    private val source: String,
    private val pubdate: String,
    private val activity: Activity
) {
    fun openNewsDetails() {
        when {
            UseChromeShared(activity).enableChrome -> {
                //Open in chrome custom tabs
                val chromeClient = NewsDetailsChrome(
                    url,
                    title,
                    img,
                    url,
                    pubdate, activity
                )
                chromeClient.openNewsDetails()
            }
            else -> {
                //No chrome installed, open in webview
                val intent = Intent(activity, NewsDetailWebView::class.java)
                intent.putExtra("url", url)
                intent.putExtra("title", title)
                intent.putExtra("img", img)
                intent.putExtra("source", source)
                intent.putExtra("pubdate", pubdate)
                activity.startActivity(intent)
            }
        }
    }
}