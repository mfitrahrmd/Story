package com.mfitrahrmd.story.data.entity

data class Story(
    val id: String,
    val author: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Float?,
    val lon: Float?
)
