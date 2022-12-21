package com.notmiyouji.newsapp.kotlin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    var vibrantLightColorList = arrayOf(
        ColorDrawable(Color.parseColor("#ffeead")),
        ColorDrawable(Color.parseColor("#93cfb3")),
        ColorDrawable(Color.parseColor("#fd7a7a")),
        ColorDrawable(Color.parseColor("#faca5f")),
        ColorDrawable(Color.parseColor("#1ba798")),
        ColorDrawable(Color.parseColor("#6aa9ae")),
        ColorDrawable(Color.parseColor("#ffbf27")),
        ColorDrawable(Color.parseColor("#d93947"))
    )
    val randomDrawbleColor: ColorDrawable
        get() {
            val idx = Random().nextInt(vibrantLightColorList.size)
            return vibrantLightColorList[idx]
        }

    @JvmStatic
    fun DateToTimeFormat(oldstringDate: String?): String? {
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
    fun DateFormat(oldstringDate: String?): String? {
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
    val country: String
        get() {
            val country =
                "US" //default use United States by default country because I'm from VietNam
            return country.lowercase(Locale.getDefault())
        }
    val language: String
        get() {
            val locale = Locale.getDefault()
            val country = locale.language.toString()
            return country.lowercase(Locale.getDefault())
        }
}