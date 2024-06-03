package com.mfitrahrmd.story.data.repository.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.datasource.IStoryDataSource
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.entity.remote.RemoteStory
import com.mfitrahrmd.story.data.mapper.toStory
import com.mfitrahrmd.story.data.repository.cache.IStoryCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class GetAllStoriesRemoteMediator(
    private val token: String,
    private val location: Boolean? = null,
    private val storyDataSource: IStoryDataSource,
    private val storyCache: IStoryCache,
) : RemoteMediator<Int, Story>() {
    private var _nextPage: Int? = null

    private suspend fun fetch(page: Int, pageSize: Int): List<RemoteStory> {
        return when (val result = storyDataSource.getAllStories(token, page, pageSize, location)) {
            is Result.Failed -> emptyList()
            is Result.Success -> result.data
        }
    }

    private suspend fun cleanLocalData() {
        storyCache.deleteAll()
    }

    private suspend fun upsertLocalData(items: List<Story>) {
        storyCache.insertMany(items)
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Story>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                STARTING_PAGE_INDEX
            }

            LoadType.APPEND -> {
                _nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = _nextPage != null)
            }

            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
        try {
            val items = fetch(page, state.config.pageSize)
            val end = items.isEmpty() || items.size < state.config.pageSize
            _nextPage = if (loadType == LoadType.REFRESH) 2
            else if (end) null
            else _nextPage?.plus(1)
            withContext(Dispatchers.IO) {
                if (loadType == LoadType.REFRESH) {
                    cleanLocalData()
                }
                upsertLocalData(items.toStory())
            }

            return MediatorResult.Success(endOfPaginationReached = end)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}
