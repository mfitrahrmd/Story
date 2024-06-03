package com.mfitrahrmd.story.data.mapper

import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.entity.db.DBStory

fun List<Story>.toDBStory(): List<DBStory> {
    return this.map {
        DBStory(
            id = it.id,
            author = it.author,
            description = it.description,
            photoUrl = it.photoUrl,
            lat = it.lat,
            lon = it.lon,
            createdAt = it.createdAt,
        )
    }
}

fun Story.toDBStory(): DBStory {
    return DBStory(
        id = this.id,
        author = this.author,
        description = this.description,
        photoUrl = this.photoUrl,
        lat = this.lat,
        lon = this.lon,
        createdAt = this.createdAt,
    )
}