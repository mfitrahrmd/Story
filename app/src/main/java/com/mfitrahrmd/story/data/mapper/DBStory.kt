package com.mfitrahrmd.story.data.mapper

import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.entity.db.DBStory

fun DBStory.toStory(): Story {
    return Story(
        id = this.id,
        author = this.author,
        description = this.description,
        photoUrl = this.photoUrl,
        lat = this.lat,
        lon = this.lat,
        createdAt = this.createdAt.toString()
    )
}