package com.mfitrahrmd.story.data.repository.cache.room

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.mapper.toDBStory
import com.mfitrahrmd.story.data.mapper.toStory
import com.mfitrahrmd.story.data.repository.cache.IStoryCache
import com.mfitrahrmd.story.data.repository.cache.room.dao.StoryDao

class StoryRoomCache(
    private val storyDao: StoryDao
) : IStoryCache {
    override fun findAll(): PagingSource<Int, Story> {
        val paging = storyDao.findAll()

        return object : PagingSource<Int, Story>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
                return when (val result = paging.load(params)) {
                    is LoadResult.Page -> {
                        LoadResult.Page(
                            data = result.data.map {
                                it.toStory()
                            },
                            prevKey = result.prevKey,
                            nextKey = result.nextKey,
                            itemsBefore = result.itemsBefore,
                            itemsAfter = result.itemsAfter
                        )
                    }

                    is LoadResult.Error -> {
                        LoadResult.Error(result.throwable)
                    }

                    is LoadResult.Invalid -> {
                        LoadResult.Invalid()
                    }
                }
            }

            // TODO : fix data does not load more after initial page
            override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
                return state.anchorPosition?.let {
                    state.closestPageToPosition(it)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
                }
            }
        }
    }

    override suspend fun deleteAll() {
        storyDao.deleteAll()
    }

    override suspend fun insertMany(items: List<Story>) {
        storyDao.insertMany(items.toDBStory())
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryRoomCache? = null

        fun getInstance(
            storyDao: StoryDao
        ): StoryRoomCache {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryRoomCache(storyDao)
                INSTANCE = instance

                instance
            }
        }
    }
}