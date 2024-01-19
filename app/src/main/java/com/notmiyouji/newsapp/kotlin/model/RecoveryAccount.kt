package com.notmiyouji.newsapp.kotlin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecoveryAccount {
    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("fullName")
    @Expose
    var fullName: String? = null

    @SerializedName("nickName")
    @Expose
    var nickName: String? = null

    @SerializedName("verify")
    @Expose
    var verify: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}