package com.mfitrahrmd.story

import android.content.Context
import com.mfitrahrmd.story.data.datasource.IAuthenticationDataSource
import com.mfitrahrmd.story.data.datasource.IStoryDataSource
import com.mfitrahrmd.story.data.datasource.datastore.AuthenticationDataStoreDataSource
import com.mfitrahrmd.story.data.datasource.datastore.authenticationDataStore
import com.mfitrahrmd.story.data.datasource.remote.AuthenticationRemoteDataSource
import com.mfitrahrmd.story.data.datasource.remote.StoryRemoteDataSource
import com.mfitrahrmd.story.data.datasource.remote.services.RemoteService
import com.mfitrahrmd.story.data.repository.AuthenticationRepository
import com.mfitrahrmd.story.data.repository.IAuthenticationRepository
import com.mfitrahrmd.story.data.repository.IStoryRepository
import com.mfitrahrmd.story.data.repository.StoryRepository
import com.mfitrahrmd.story.data.repository.cache.dao.database.StoryDatabase
import com.mfitrahrmd.story.data.util.FileProvider
import com.mfitrahrmd.story.data.util.IFileProvider

class ApplicationContainer(
    private val context: Context
) : IApplicationContainer {
    private val remoteService: RemoteService by lazy {
        RemoteService.getInstance()
    }
    private val storyDatabase: StoryDatabase by lazy {
        StoryDatabase.getInstance(context)
    }
    private val storyPhotosFileProvider: IFileProvider by lazy {
        FileProvider(context, STORY_PHOTOS_FILE_PATH)
    }
    private val authenticationRemoteDataSource: IAuthenticationDataSource by lazy {
        AuthenticationRemoteDataSource.getInstance(remoteService)
    }
    private val storyRemoteDataSource: IStoryDataSource by lazy {
        StoryRemoteDataSource.getInstance(remoteService)
    }
    override val authenticationRepository: IAuthenticationRepository by lazy {
        AuthenticationRepository.getInstance(authenticationRemoteDataSource)
    }
    override val storyRepository: IStoryRepository by lazy {
        StoryRepository.getInstance(storyRemoteDataSource, storyDatabase.storyDao(), storyPhotosFileProvider)
    }
    override val authentication: AuthenticationDataStoreDataSource by lazy {
        AuthenticationDataStoreDataSource.getInstance(context.authenticationDataStore)
    }
}