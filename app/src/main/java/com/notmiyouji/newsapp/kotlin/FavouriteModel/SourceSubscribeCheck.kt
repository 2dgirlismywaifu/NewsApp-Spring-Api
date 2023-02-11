package com.notmiyouji.newsapp.kotlin.FavouriteModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class SourceSubscribeCheck {
    //SerializedName and Expose: user_id, source_id, status
    @SerializedName("user_id")
    @Expose
    var userId: String? = null
    @SerializedName("source_id")
    @Expose
    var sourceId: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
}