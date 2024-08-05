package com.mfitrahrmd.story

import com.mfitrahrmd.story.data.datasource.datastore.SessionDataStoreDataSource
import com.mfitrahrmd.story.data.repository.IAuthenticationRepository
import com.mfitrahrmd.story.data.repository.IStoryRepository

interface IApplicationContainer {
    val authenticationRepository: IAuthenticationRepository
    val storyRepository: IStoryRepository
    val session: SessionDataStoreDataSource
}