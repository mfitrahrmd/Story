package com.mfitrahrmd.story.data.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.entity.remote.RemoteStory
import com.mfitrahrmd.story.data.mapper.toStory

class StoryPagingSource(
    private val fetcher: Fetcher
) : PagingSource<Int, Story>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: STARTING_PAGE_INDEX
            when (val result = fetcher(page, params.loadSize)) {
                is Result.Success -> {
                    val position = params.key ?: STARTING_PAGE_INDEX
                    val prevKey = if (position == STARTING_PAGE_INDEX) null else (position - 1)
                    val end = result.data.isEmpty() || result.data.size < params.loadSize
                    val nextKey =
                        if (position == STARTING_PAGE_INDEX) 2 else if (end) null else position.plus(
                            1
                        )

                    LoadResult.Page(
                        data = result.data.toStory(),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                is Result.Failed -> {
                    LoadResult.Error(
                        Error(result.message)
                    )
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // TODO : fix data does not load more after initial page
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    fun interface Fetcher {
        suspend operator fun invoke(page: Int, size: Int): Result<List<RemoteStory>>
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}