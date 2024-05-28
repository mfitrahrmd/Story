package com.mfitrahrmd.story.data.repositories

import com.mfitrahrmd.story.data.entities.User

interface IAuthenticationRepository {
    suspend fun register(): Boolean
    suspend fun login(userAccount: User.Account): User.Account
}