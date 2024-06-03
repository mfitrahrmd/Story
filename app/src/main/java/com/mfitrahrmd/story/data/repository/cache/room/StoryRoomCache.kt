package com.mfitrahrmd.story.data.repository.cache.room

import androidx.paging.PagingSource
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.mapper.toDBStory
import com.mfitrahrmd.story.data.repository.cache.IStoryCache
import com.mfitrahrmd.story.data.repository.cache.room.dao.StoryDao

class StoryRoomCache(
    private val storyDao: StoryDao
) : IStoryCache {
    override fun findAll(): PagingSource<Int, Story> {
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