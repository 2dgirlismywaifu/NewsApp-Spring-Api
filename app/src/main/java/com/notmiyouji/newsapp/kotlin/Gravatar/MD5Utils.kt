package com.notmiyouji.newsapp.kotlin.Gravatar

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MD5Utils {
    fun hex(array: ByteArray): String {
        val sb = StringBuffer()
        for (i in array.indices) {
            sb.append(Integer.toHexString((array[i]
                    .toInt()
                    and 0xFF) or 0x100).substring(1, 3))
        }
        return sb.toString()
    }

    @JvmStatic
    fun md5Hex(message: String): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            return hex(md.digest(message.toByteArray(charset("CP1252"))))
        } catch (e: NoSuchAlgorithmException) {
        } catch (e: UnsupportedEncodingException) {
        }
        return null
    }
}