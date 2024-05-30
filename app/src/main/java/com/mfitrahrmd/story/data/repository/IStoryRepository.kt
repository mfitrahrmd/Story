package com.mfitrahrmd.story.data.repository

import androidx.paging.PagingData
import com.mfitrahrmd.story.data.entity.Story
import kotlinx.coroutines.flow.Flow

interface IStoryRepository {
    suspend fun createStory(story: Story): Boolean
    suspend fun getAllStories(page: Int?, size: Int?, location: Boolean?): Flow<PagingData<Story>>
    suspend fun getDetailStory(storyId: String): Story?
}