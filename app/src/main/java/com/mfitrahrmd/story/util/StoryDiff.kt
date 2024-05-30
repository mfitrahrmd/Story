package com.mfitrahrmd.story.util

import androidx.recyclerview.widget.DiffUtil
import com.mfitrahrmd.story.data.entity.Story

object StoryDiff : DiffUtil.ItemCallback<Story>() {
    override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
        return newItem == oldItem
    }
}