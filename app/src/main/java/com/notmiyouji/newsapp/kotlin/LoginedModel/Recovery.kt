package com.notmiyouji.newsapp.kotlin.LoginedModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Recovery (var email: String?) {
    @SerializedName("recoverycode")
    @Expose
    var recoverycode: String? = null
}