package com.notmiyouji.newsapp.kotlin.RSSFeed

class Item(
    var title: String,
    var pubDate: String,
    var link: String,
    var guid: String,
    var author: String,
    var thumbnail: String,
    var description: String,
    var content: String,
    var enclosure: Any,
    var categories: List<String>
)