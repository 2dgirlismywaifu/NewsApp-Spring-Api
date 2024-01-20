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

package com.notmiyouji.newsapp.kotlin.util

import android.util.Log
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class GravatarImage(var email: String) {

    private fun hex(array: ByteArray): String {
        val sb = StringBuffer()
        for (i in array.indices) {
            sb.append(
                Integer.toHexString(
                    (array[i]
                        .toInt()
                            and 0xFF) or 0x100
                ).substring(1, 3)
            )
        }
        return sb.toString()
    }


    private fun md5Hex(message: String): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            return hex(md.digest(message.toByteArray(charset("CP1252"))))
        } catch (e: NoSuchAlgorithmException) {
            Log.e("MD5Utils", "md5Hex: ", e)
        } catch (e: UnsupportedEncodingException) {
            Log.e("MD5Utils", "md5Hex: ", e)
        }
        return null
    }

    val gravatarURL: String
        get() {
            val hash = md5Hex(email)
            return "https://www.gravatar.com/avatar/$hash?s=400&d=404"
        }
}