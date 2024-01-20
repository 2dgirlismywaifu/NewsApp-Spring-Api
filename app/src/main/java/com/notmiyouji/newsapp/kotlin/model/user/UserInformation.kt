package com.notmiyouji.newsapp.kotlin.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserInformation {

    @SerializedName("userName")
    @Expose
    var userName: String? = null

    @SerializedName("fullName")
    @Expose
    var fullName: String? = null

    @SerializedName("birthday")
    @Expose
    var birthday: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}