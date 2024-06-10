package com.mfitrahrmd.story.data.repository

import androidx.core.net.toUri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.datasource.IStoryDataSource
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.mapper.toStory
import com.mfitrahrmd.story.data.repository.pagingsource.StoryPagingSource
import com.mfitrahrmd.story.data.util.IFileProvider
import kotlinx.coroutines.flow.Flow

class StoryRepository private constructor(
    private val storyDataSource: IStoryDataSource,
    private val fileProvider: IFileProvider
) : IStoryRepository {
    override suspend fun createStory(story: Story): Result<Boolean> {
        val storyPhotoFile = fileProvider.uriToJpgFile(story.photoUrl.toUri())

        return storyDataSource.createStory(story, storyPhotoFile)
    }

    override suspend fun createStory(token: String, story: Story): Result<Boolean> {
        val storyPhotoFile = fileProvider.uriToJpgFile(story.photoUrl.toUri())

        return storyDataSource.createStory(token, story, storyPhotoFile)
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
            is Result.Failed -> result
            is Result.Success -> Result.Success(result.data.toStory())
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_MAX_SIZE = 50

        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(
            storyDataSource: IStoryDataSource,
            fileProvider: IFileProvider
        ): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryRepository(storyDataSource, fileProvider)
                INSTANCE = instance

                instance
            }
        }
    }
}