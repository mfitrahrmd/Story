package com.mfitrahrmd.story.data.datasource

import com.mfitrahrmd.story.data.entities.User

interface IAuthenticationDataSource {
    suspend fun register(user: User): Boolean
    suspend fun login(userAccount: User.Account): User.Account
}