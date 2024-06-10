package com.mfitrahrmd.story.ui.adapter

import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.databinding.ItemStoryBinding
import com.mfitrahrmd.story.util.StoryDiff
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(StoryDiff) {
    inner class StoryViewHolder(
        private val viewBinding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(story: Story?) {
            if (story != null) {
                with(viewBinding) {
                    tvName.text = story.author
                    // date logic
                    val then = Instant.parse(story.createdAt).atZone(ZoneId.systemDefault())
                    val now = ZonedDateTime.now(ZoneId.systemDefault())
                    val duration = Duration.between(then, now)
                    val minute = 60
                    val hour = minute * 60
                    val day = hour * 24
                    val week = day * 7
                    val month = day * 30
                    val time = if (duration.seconds < minute) "${duration.seconds} seconds ago"
                    else if (duration.seconds < hour) "${duration.toMinutes()} minutes ago"
                    else if (duration.seconds < day) "${duration.toHours()} hours ago"
                    else if (duration.seconds < week) "${duration.toDays() / 7} weeks ago"
                    else if (duration.seconds < month) "${duration.toDays() / 30} months ago"
                    else "${duration.toDays() / 365} years ago"
                    tvTime.text = time
                    // text length logic
                    val avgWPM = 200
                    val words = story.description.split("\\s+".toRegex()).size
                    val readingTimeMinutes = words / avgWPM
                    tvLength.text = when {
                        readingTimeMinutes <= 1 -> "1 minute read"
                        else -> "$readingTimeMinutes minutes read"
                    }
                    // location logic
                    if (story.lat != null && story.lon != null && story.lat.toDouble() != 0.0 && story.lon.toDouble() != 0.0) {
                        val geocoder = Geocoder(itemView.context, Locale.getDefault())
                        geocoder.getFromLocation(story.lat.toDouble(), story.lon.toDouble(), 1)
                            ?.let {
                                if (it.isEmpty()) return@let
                                val cityName = it[0].getAddressLine(0)
                                val stateName = it[0].getAddressLine(1)
                                val countryName = it[0].getAddressLine(2)
                                tvLocation.text = "$cityName ~ ${it[0].adminArea} ${it[0].subAdminArea}"
                            }
                    } else {
                        tvLocation.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }
}