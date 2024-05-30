package com.mfitrahrmd.story.data.repository

import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.datasource.IAuthenticationDataSource
import com.mfitrahrmd.story.data.entity.User

class AuthenticationRepository private constructor(
    private val authenticationDataSource: IAuthenticationDataSource
) : IAuthenticationRepository {
    override suspend fun register(user: User): Result<Boolean> {
        return authenticationDataSource.register(user)
    }

    override suspend fun login(userAccount: User.Account): Result<User.Account> {
        return authenticationDataSource.login(userAccount)
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthenticationRepository? = null

        fun getInstance(authenticationDataSource: IAuthenticationDataSource): AuthenticationRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthenticationRepository(authenticationDataSource)
                INSTANCE = instance

                instance
            }
        }
    }

}