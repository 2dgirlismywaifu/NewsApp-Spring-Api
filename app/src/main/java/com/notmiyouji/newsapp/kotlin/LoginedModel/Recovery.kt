package com.notmiyouji.newsapp.kotlin.LoginedModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Recovery {
    @SerializedName("recoverycode")
    @Expose
    var recoveryCode: String? = null
}