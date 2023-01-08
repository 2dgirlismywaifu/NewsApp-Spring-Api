package com.notmiyouji.newsapp.kotlin.NewsAPIModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Country {
    @SerializedName("country_id")
    @Expose
    var countryId: String? = null

    @SerializedName("country_code")
    @Expose
    var countryCode: String? = null

    @SerializedName("country_name")
    @Expose
    var countryName: String? = null
}