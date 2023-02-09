package com.notmiyouji.newsapp.kotlin.UpdateModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Gender {
    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}