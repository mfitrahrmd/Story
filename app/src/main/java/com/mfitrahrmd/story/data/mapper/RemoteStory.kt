package com.mfitrahrmd.story.data.mapper

import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.entity.db.DBStory
import com.mfitrahrmd.story.data.entity.remote.RemoteStory
import com.mfitrahrmd.story.util.DateFormat

fun RemoteStory.toStory(): Story {
    return Story(
        id = this.id,
        author = this.name,
        description = this.description,
        photoUrl = this.photoUrl,
        lat = this.lat,
        lon = this.lon,
        createdAt = this.createdAt,
        _postedAt = null,
        _readingTime = null,
        _locationName = null
    )
}

fun List<RemoteStory>.toDBStory(): List<DBStory> {
    return this.map {
        DBStory(
            id = it.id,
            author = it.name,
            description = it.description,
            photoUrl = it.photoUrl,
            lat = it.lat,
            lon = it.lon,
            createdAt = DateFormat.toDate(it.createdAt),
        )
    }
}

fun List<RemoteStory>.toStory(): List<Story> {
    return this.map {
        Story(
            id = it.id,
            author = it.name,
            description = it.description,
            photoUrl = it.photoUrl,
            lat = it.lat,
            lon = it.lon,
            createdAt = it.createdAt,
            _locationName = null,
            _postedAt = null,
            _readingTime = null
        )
    }
}