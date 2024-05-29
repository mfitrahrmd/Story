package com.mfitrahrmd.story.data.datasource.remote.dto

import com.mfitrahrmd.story.data.entities.remote.RemoteStory

class GetDetailStoryResponse(error: Boolean, message: String, val story: RemoteStory) :
    BaseResponse(error, message)