package com.mfitrahrmd.story.usecase

import com.mfitrahrmd.story.data.entities.User

interface IAuthenticationUseCase {
    suspend fun register(user: User): Boolean
    suspend fun login(userAccount: User.Account): User.Account
}