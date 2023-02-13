/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notmiyouji.newsapp.kotlin.LoginedModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignIn {
    @SerializedName("sync_settings")
    @Expose
    var syncSettings: Any? = null

    @SerializedName("salt")
    @Expose
    var salt: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null

    @SerializedName("recovery")
    @Expose
    var recovery: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("birthaday")
    @Expose
    var birthday: String? = null

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