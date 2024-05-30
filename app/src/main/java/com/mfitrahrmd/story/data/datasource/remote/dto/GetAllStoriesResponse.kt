package com.mfitrahrmd.story.data.datasource.remote.dto

import com.mfitrahrmd.story.data.entity.remote.RemoteStory

class GetAllStoriesResponse(
    error: Boolean,
    message: String,
    val listStory: List<RemoteStory>
) : BaseResponse(error, message)