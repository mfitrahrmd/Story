package com.mfitrahrmd.story.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormat {
    fun toDate(date: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val x = formatter.parse(date)

        return x
    }
}