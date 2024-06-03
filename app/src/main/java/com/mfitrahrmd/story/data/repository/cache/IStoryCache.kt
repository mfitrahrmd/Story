package com.mfitrahrmd.story.data.repository.cache

import androidx.paging.PagingSource
import com.mfitrahrmd.story.data.entity.Story

interface IStoryCache {
    fun findAll(): PagingSource<Int, Story>
    suspend fun deleteAll()
    suspend fun insertMany(items: List<Story>)
}