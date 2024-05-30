package com.mfitrahrmd.story.data.datasource

import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.entity.remote.RemoteStory
import java.io.File

interface IStoryDataSource {
    suspend fun createStory(story: Story, photo: File): Result<Boolean>
    suspend fun createStory(token: String, story: Story, photo: File): Result<Boolean>
    suspend fun getAllStories(token: String, page: Int?, size: Int?, location: Boolean?): Result<List<RemoteStory>>
    suspend fun getDetailStory(token: String, storyId: String): Result<RemoteStory>
}