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

package com.notmiyouji.newsapp.kotlin.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignIn {
    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("nickname")
    @Expose
    var nickName: String? = null

    @SerializedName("verify")
    @Expose
    var verify: String? = null

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

    @SerializedName("status")
    @Expose
    var status: String? = null
}