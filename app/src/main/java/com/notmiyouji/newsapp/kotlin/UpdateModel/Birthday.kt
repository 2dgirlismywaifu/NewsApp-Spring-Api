package com.notmiyouji.newsapp.kotlin.UpdateModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Birthday {
    @SerializedName("birthday")
    @Expose
    var birthday: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}