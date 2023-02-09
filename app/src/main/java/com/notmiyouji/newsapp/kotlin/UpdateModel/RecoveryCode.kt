package com.notmiyouji.newsapp.kotlin.UpdateModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecoveryCode {
    @SerializedName("recoverycode")
    @Expose
    var code: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}