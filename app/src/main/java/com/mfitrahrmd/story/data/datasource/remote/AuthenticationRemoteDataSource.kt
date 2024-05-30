package com.mfitrahrmd.story.data.datasource.remote

import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.datasource.IAuthenticationDataSource
import com.mfitrahrmd.story.data.datasource.remote.services.NetworkResponse
import com.mfitrahrmd.story.data.datasource.remote.services.RemoteService
import com.mfitrahrmd.story.data.entity.User
import com.mfitrahrmd.story.data.mapper.toLoginRequest
import com.mfitrahrmd.story.data.mapper.toRegisterRequest

class AuthenticationRemoteDataSource private constructor(
    private val remoteService: RemoteService
) : IAuthenticationDataSource {
    override suspend fun register(user: User): Result<Boolean> {
        val result = remoteService.authenticationService.register(user.toRegisterRequest())
        return when (result) {
            is NetworkResponse.ApiError -> Result.Failed(result.body.message)
            is NetworkResponse.NetworkError -> Result.Failed(result.error.message.orEmpty())
            is NetworkResponse.Success -> Result.Success(true)
            is NetworkResponse.UnknownError -> Result.Failed(result.error?.message.orEmpty())
        }
    }

    override suspend fun login(userAccount: User.Account): Result<User.Account> {
        val result = remoteService.authenticationService.login(userAccount.toLoginRequest())
        return when (result) {
            is NetworkResponse.ApiError -> Result.Failed(result.body.message)
            is NetworkResponse.NetworkError -> Result.Failed(result.error.message.orEmpty())
            is NetworkResponse.Success -> Result.Success(
                User.Account(
                    userAccount.email,
                    userAccount.password,
                    result.body.loginResult.token
                )
            )

            is NetworkResponse.UnknownError -> Result.Failed(result.error?.message.orEmpty())
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthenticationRemoteDataSource? = null

        fun getInstance(remoteService: RemoteService): AuthenticationRemoteDataSource {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthenticationRemoteDataSource(remoteService)
                INSTANCE = instance

                instance
            }
        }
    }
}