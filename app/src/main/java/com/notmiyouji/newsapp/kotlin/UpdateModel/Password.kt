package com.notmiyouji.newsapp.kotlin.UpdateModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Password {
    @SerializedName("newpass")
    @Expose
    var password: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}