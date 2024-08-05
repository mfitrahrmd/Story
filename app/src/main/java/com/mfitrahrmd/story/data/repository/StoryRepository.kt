package com.mfitrahrmd.story.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.datasource.IStoryDataSource
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.mapper.toStory
import com.mfitrahrmd.story.data.repository.pagingsource.StoryPagingSource
import kotlinx.coroutines.flow.Flow
import java.io.File

class StoryRepository private constructor(
    private val storyDataSource: IStoryDataSource
) : IStoryRepository {
    override suspend fun createStory(story: Story, imageFile: File): Result<Boolean> {
        return storyDataSource.createStory(story, imageFile)
    }

    override suspend fun createStory(
        token: String,
        story: Story,
        imageFile: File
    ): Result<Boolean> {
        return storyDataSource.createStory(token, story, imageFile)
    }

    override suspend fun getStoryPages(
        token: String,
        page: Int?,
        size: Int?,
        location: Boolean?
    ): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, maxSize = DEFAULT_MAX_SIZE),
            pagingSourceFactory = {
                StoryPagingSource { page, size ->
                    storyDataSource.getAllStories(token, page, size, location)
                }
            },
        ).flow
    }

    override suspend fun getDetailStory(token: String, storyId: String): Result<Story> {
        return when (val result = storyDataSource.getDetailStory(token, storyId)) { // type mismatch
            is Result.Success.WithData -> Result.Success.WithData(
                result.message,
                result.data.toStory()
            )

            is Result.Success.Message -> result
            is Result.Failed -> result
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_MAX_SIZE = 50

        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(
            storyDataSource: IStoryDataSource,
        ): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryRepository(storyDataSource)
                INSTANCE = instance

                instance
            }
        }
    }
}