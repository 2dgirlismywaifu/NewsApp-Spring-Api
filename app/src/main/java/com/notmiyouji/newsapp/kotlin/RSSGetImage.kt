package com.notmiyouji.newsapp.kotlin

import org.jsoup.Jsoup

class RSSGetImage {
    //Thanks, JSoup!
    var urlLink: String? = null
    val srcImg: String?
        get() {
            val doc = urlLink?.let { Jsoup.parse(it) }
            val elements = doc?.select("img")
            if (elements != null) {
                for (element in elements) {
                    return element.attr("src")
                }
            }
            return null
        }
}