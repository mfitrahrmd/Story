package com.mfitrahrmd.story.data.datasource

import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.entities.User

interface IAuthenticationDataSource {
    suspend fun register(user: User): Result<Boolean>
    suspend fun login(userAccount: User.Account): Result<User.Account>
}