package com.notmiyouji.newsapp.kotlin.LoginedModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckNickName {
    @SerializedName("nickname")
    @Expose
    var nickname: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
}