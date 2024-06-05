package com.mfitrahrmd.story.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.databinding.ItemStoryBinding
import com.mfitrahrmd.story.util.StoryDiff
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(StoryDiff) {
    inner class StoryViewHolder(
        private val viewBinding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(story: Story?) {
            if (story != null) {
                with(viewBinding) {
                    tvName.text = story.author
                    tvLocation.text = "${story.lat} ~ ${story.lon}"
                    tvLength.text = "5 minutes read"
                    val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    val offsetDateTime = OffsetDateTime.parse(story.createdAt, inputFormatter)
                    val targetOffset = ZoneOffset.ofHours(7)
                    val then = offsetDateTime
                        .withOffsetSameInstant(targetOffset)
                    val now = Date().toInstant().atOffset(ZoneOffset.UTC).withOffsetSameInstant(
                        ZoneOffset.ofHours(7))
                    val duration = Duration.between(then, now)
                    val minute = 60
                    val hour = minute * 60
                    val day = hour * 24
                    val week = day * 7
                    val month = day * 30
                    val time = if (duration.seconds < minute) "${duration.seconds} seconds ago"
                    else if (duration.seconds < hour) "${duration.toMinutes()} minutes ago"
                    else if (duration.seconds < day) "${duration.toHours()} hours ago"
                    else if (duration.seconds < week) "${duration.toDays()/7} weeks ago"
                    else if (duration.seconds < month) "${duration.toDays()/30} months ago"
                    else "${duration.toDays()/365} years ago"
                    tvTime.text = time
                }
            }
        }
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(ItemStoryBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        ))
    }
}