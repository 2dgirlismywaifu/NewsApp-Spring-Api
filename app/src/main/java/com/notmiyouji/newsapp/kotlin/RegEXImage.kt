package com.notmiyouji.newsapp.kotlin

import org.jsoup.Jsoup

class RegEXImage(var description: String, var thumbnail: String?) {
    fun regEXImage(): String? {
        return if (thumbnail != null) {
            val doc = Jsoup.parse(description)
            val elements = doc.select("img")
            var src: String? = null
            for (element in elements) {
                src = element.attr("src")
            }
            src
        }
        else {
            thumbnail
        }
    }
}