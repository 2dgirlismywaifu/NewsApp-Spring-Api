package com.notmiyouji.newsapp.kotlin

import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    @JvmStatic
    fun dateToTimeFormat(oldstringDate: String?): String? {
        val p = PrettyTime(Locale(country))
        var isTime: String? = null
        try {
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.getDefault()
            )
            val date = oldstringDate?.let { sdf.parse(it) }
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
}