package com.notmiyouji.newsapp.kotlin.LoginedModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountSSO {
    @SerializedName("sync_settings")
    @Expose
    var syncSettings: Any? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("birthaday")
    @Expose
    var birthaday: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("nickname")
    @Expose
    var nickname: String? = null

    @SerializedName("verify")
    @Expose
    var verify: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}