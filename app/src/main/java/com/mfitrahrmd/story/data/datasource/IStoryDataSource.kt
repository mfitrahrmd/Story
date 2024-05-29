package com.mfitrahrmd.story.data.datasource

import androidx.paging.PagingData
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.entities.Story
import com.mfitrahrmd.story.data.entities.remote.RemoteStory
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IStoryDataSource {
    suspend fun createStory(story: Story, photo: File): Result<Boolean>
    suspend fun createStory(token: String, story: Story, photo: File): Result<Boolean>
    suspend fun getAllStories(token: String, page: Int?, size: Int?, location: Boolean?): Result<List<RemoteStory>>
    suspend fun getDetailStory(token: String, storyId: String): Result<RemoteStory>
}