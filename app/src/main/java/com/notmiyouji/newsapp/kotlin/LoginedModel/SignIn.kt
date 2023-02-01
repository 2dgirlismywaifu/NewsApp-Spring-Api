package com.notmiyouji.newsapp.kotlin.LoginedModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignIn {
    @SerializedName("sync_settings")
    @Expose
    var syncSettings: Any? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("salt")
    @Expose
    var salt: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("nickname")
    @Expose
    var nickname: String? = null

    @SerializedName("verify")
    @Expose
    var verify: String? = null

    @SerializedName("recovery")
    @Expose
    var recovery: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}