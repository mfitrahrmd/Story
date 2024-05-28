package com.mfitrahrmd.story.usecase

import com.mfitrahrmd.story.data.entities.Story

interface IStoryUseCase {
    suspend fun createStory(story: Story): Boolean
    suspend fun getAllStories(page: Int?, size: Int?, location: Boolean?): List<Story>
    suspend fun getDetailStory(storyId: String): Story?
}