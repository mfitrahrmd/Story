package com.mfitrahrmd.story.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.databinding.ItemStoryBinding
import com.mfitrahrmd.story.util.StoryDiff

class StoryAdapter(
    private val context: Context
) : PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(StoryDiff) {
    inner class StoryViewHolder(
        private val viewBinding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(story: Story?) {
            if (story != null) {
                with(viewBinding) {
                    tvName.text = story.author
                    if (story.readingTime != null) {
                        tvLength.text = story.readingTime
                    } else {
                        tvLength.visibility = View.GONE
                    }
                    if (story.postedAt != null) {
                        tvTime.text = story.postedAt
                    } else {
                        tvTime.visibility = View.GONE
                    }
                    if (story.locationName != null) {
                        tvLocation.text = story.locationName
                    } else {
                        tvLocation.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        story?.setPostedAtFromCreatedAt(context)
        story?.setLocationNameFromLatLon(context)
        story?.setReadingTimeFromDescription()
        holder.bind(story)
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