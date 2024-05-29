package com.mfitrahrmd.story.data.entities.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RemoteStory(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Float,
    val lon: Float
): Parcelable
