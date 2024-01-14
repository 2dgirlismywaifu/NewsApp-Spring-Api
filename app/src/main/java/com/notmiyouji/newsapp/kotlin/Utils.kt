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

package com.notmiyouji.newsapp.kotlin

import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    @JvmStatic
    fun dateToTimeFormat(oldStringDate: String?): String? {
        val p = PrettyTime(Locale(country))
        var isTime: String? = null
        try {
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.getDefault()
            )
            val date = oldStringDate?.let { sdf.parse(it) }
            isTime = p.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return isTime
    }

    @Deprecated("Use DateToTimeFormat instead", ReplaceWith("DateToTimeFormat(oldstringDate)"))
    fun dateFormat(oldstringDate: String?): String? {
        val newDate: String?
        val dateFormat = SimpleDateFormat("E, d MMM yyyy", Locale(country))
        newDate = try {
            val date = oldstringDate?.let { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(it) }
            date?.let { dateFormat.format(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
            oldstringDate
        }
        return newDate
    }

    //default use United States by default country because I'm from VietNam
    private val country: String
        get() {
            val country =
                "US" //default use United States by default country because I'm from VietNam
            return country.lowercase(Locale.getDefault())
        }

    //Encode to base64
    @JvmStatic
    fun encodeToBase64(data: String): String {
        return String(android.util.Base64.encode(data.toByteArray(), android.util.Base64.DEFAULT))
    }
}