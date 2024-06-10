package com.mfitrahrmd.story.data.entity

import android.content.Context
import android.location.Geocoder
import com.mfitrahrmd.story.R
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale

data class Story(
    val id: String,
    val author: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Float?,
    val lon: Float?,
    private var _postedAt: String?,
    private var _locationName: String?,
    private var _readingTime: String?,
) {
    val postedAt: String?
        get() = _postedAt
    val locationName: String?
        get() = _locationName
    val readingTime: String?
        get() = _readingTime

    fun setPostedAtFromCreatedAt(context: Context) {
        val then = Instant.parse(this.createdAt).atZone(ZoneId.systemDefault())
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val duration = Duration.between(then, now)
        val minute = 60
        val hour = minute * 60
        val day = hour * 24
        val week = day * 7
        val month = day * 30
        val year = month * 12
        val postedAt = if (duration.seconds < minute) context.resources.getQuantityString(
            R.plurals.second_ago,
            duration.seconds.toInt(),
            duration.seconds
        )
        else if (duration.seconds < hour) context.resources.getQuantityString(
            R.plurals.minute_ago,
            duration.toMinutes().toInt(),
            duration.toMinutes()
        )
        else if (duration.seconds < day) context.resources.getQuantityString(
            R.plurals.hour_ago,
            duration.toHours().toInt(),
            duration.toHours()
        )
        else if (duration.seconds < week) context.resources.getQuantityString(
            R.plurals.day_ago,
            duration.toDays().toInt(),
            duration.toDays()
        )
        else if (duration.seconds < month) context.resources.getQuantityString(
            R.plurals.week_ago,
            (duration.toDays() / 7).toInt(), duration.toDays() / 7
        )
        else if (duration.seconds < year) context.resources.getQuantityString(
            R.plurals.month_ago,
            (duration.toDays() / 30).toInt(), duration.toDays() / 30
        )
        else context.resources.getQuantityString(
            R.plurals.year_ago,
            (duration.toDays() / year).toInt(), duration.toDays() / year
        )
        this._postedAt = postedAt
    }

    fun setLocationName(locationName: String) {
        this._locationName = locationName
    }

    fun setReadingTimeFromDescription(context: Context) {
        val avgWPM = 200
        val words = this.description.split("\\s+".toRegex()).size
        val readingTimeMinutes = if (words < 200) 1 else words / avgWPM
        this._readingTime = context.resources.getQuantityString(
            R.plurals.minute_read,
            readingTimeMinutes,
            readingTimeMinutes
        )
    }
}
