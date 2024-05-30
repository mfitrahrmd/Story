package com.mfitrahrmd.story.data.repository

import com.mfitrahrmd.story.data.datasource.IAuthenticationDataSource
import com.mfitrahrmd.story.data.entity.User
import com.mfitrahrmd.story.data.Result

class AuthenticationRepository(
    private val authenticationDataSource: IAuthenticationDataSource
) : IAuthenticationRepository {
    override suspend fun register(user: User): Result<Boolean> {
        return authenticationDataSource.register(user)
    }

    override suspend fun login(userAccount: User.Account): Result<User.Account> {
        return authenticationDataSource.login(userAccount)
    }
}