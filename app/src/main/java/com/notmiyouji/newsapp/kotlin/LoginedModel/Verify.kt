package com.notmiyouji.newsapp.kotlin.LoginedModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Verify {
    @SerializedName("verify")
    @Expose
    var verifyStatus: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
}