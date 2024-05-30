package com.mfitrahrmd.story.data.repository

import com.mfitrahrmd.story.data.entity.User
import com.mfitrahrmd.story.data.Result

interface IAuthenticationRepository {
    suspend fun register(user: User): Result<Boolean>
    suspend fun login(userAccount: User.Account): Result<User.Account>
}