package com.mfitrahrmd.story.data.datasource

import androidx.paging.PagingData
import com.mfitrahrmd.story.data.entities.Story
import kotlinx.coroutines.flow.Flow

interface IStoryDataSource {
    suspend fun createStory(story: Story): Boolean
    suspend fun getAllStories(page: Int?, size: Int?, location: Boolean?): List<Story>
    suspend fun getDetailStory(storyId: String): Story?
}