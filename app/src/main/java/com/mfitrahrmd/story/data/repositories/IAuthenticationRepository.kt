package com.mfitrahrmd.story.data.repositories

import com.mfitrahrmd.story.data.entities.User
import com.mfitrahrmd.story.data.Result

interface IAuthenticationRepository {
    suspend fun register(user: User): Result<Boolean>
    suspend fun login(userAccount: User.Account): Result<User.Account>
}