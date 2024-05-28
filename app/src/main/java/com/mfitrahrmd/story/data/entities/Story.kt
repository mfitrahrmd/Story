package com.mfitrahrmd.story.data.entities

import java.util.Date

data class Story(
    val id: String,
    val author: String,
    val description: String,
    val photoUrl: String,
    val createdAt: Date,
    val lat: Float,
    val lon: Float
)
