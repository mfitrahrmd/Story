package com.mfitrahrmd.story.data.repository

import androidx.paging.PagingData
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.entity.Story
import kotlinx.coroutines.flow.Flow

interface IStoryRepository {
    suspend fun createStory(token: String, story: Story): Result<Boolean>
    suspend fun createStory(story: Story): Result<Boolean>
    suspend fun getAllStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Boolean?
    ): Flow<PagingData<Story>>

    suspend fun getDetailStory(token: String, storyId: String): Result<Story>
}