package com.mfitrahrmd.story.data.repository

import androidx.paging.PagingData
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.entity.Story
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IStoryRepository {
    suspend fun createStory(token: String, story: Story, imageFile: File): Result<Boolean>
    suspend fun createStory(story: Story, imageFile: File): Result<Boolean>
    suspend fun getStoryPages(
        token: String,
        page: Int?,
        size: Int?,
        location: Boolean?
    ): Flow<PagingData<Story>>

    suspend fun getDetailStory(token: String, storyId: String): Result<Story>
}