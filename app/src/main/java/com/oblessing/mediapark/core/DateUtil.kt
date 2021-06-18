package com.oblessing.mediapark.core

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private val simpleDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    }

    fun stringToIsoDate(value: String): String {
        try {
            if (value.isBlank()) {
                return "None"
            }
            val date = simpleDateFormat.parse(value.trim())
            return date?.let {
                SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH
                ).format(date)
            } ?: "None"
        } catch (e: Exception) {
            e.printStackTrace()

            return "None"
        }
    }

    fun fromIsoToDate(value: String): String {
        if (value == "None") {
            return ""
        }
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).parse(value)
        return date?.let { simpleDateFormat.format(date) } ?: ""
    }
}