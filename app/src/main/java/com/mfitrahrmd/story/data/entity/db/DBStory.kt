package com.mfitrahrmd.story.data.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "stories")
data class DBStory(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int = 0,
    val id: String,
    val author: String,
    val description: String,
    val photoUrl: String,
    val createdAt: Date,
    val lat: Float?,
    val lon: Float?
)