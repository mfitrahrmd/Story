package com.mfitrahrmd.story

import android.content.Context
import com.mfitrahrmd.story.data.datasource.IAuthenticationDataSource
import com.mfitrahrmd.story.data.datasource.remote.AuthenticationRemoteDataSource
import com.mfitrahrmd.story.data.datasource.remote.services.RemoteService
import com.mfitrahrmd.story.data.repository.AuthenticationRepository
import com.mfitrahrmd.story.data.repository.IAuthenticationRepository

class ApplicationContainer(
    private val context: Context
) : IApplicationContainer {
    private val remoteService: RemoteService by lazy {
        RemoteService.getInstance()
    }
    private val authenticationRemoteDataSource: IAuthenticationDataSource by lazy {
        AuthenticationRemoteDataSource.getInstance(remoteService)
    }
    override val authenticationRepository: IAuthenticationRepository by lazy {
        AuthenticationRepository(authenticationRemoteDataSource)
    }
}