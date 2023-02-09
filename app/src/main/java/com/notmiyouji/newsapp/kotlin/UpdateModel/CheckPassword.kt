package com.notmiyouji.newsapp.kotlin.UpdateModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckPassword {
    @SerializedName("status")
    @Expose
    var status: String? = null
}