package com.notmiyouji.newsapp.kotlin.Gravatar

import com.notmiyouji.newsapp.kotlin.Gravatar.MD5Utils.md5Hex

class RequestImage(var email: String) {
    val gravatarURL: String
        get() {
            val hash = md5Hex(email)
            return "https://www.gravatar.com/avatar/$hash?s=400&d=404"
        }
}