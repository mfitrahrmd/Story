package com.mfitrahrmd.story

import androidx.lifecycle.ViewModel
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.datasource.datastore.SessionDataStoreDataSource
import com.mfitrahrmd.story.data.entity.User
import com.mfitrahrmd.story.data.repository.IAuthenticationRepository

class MainActivityViewModel(
    private val authenticationRepository: IAuthenticationRepository,
    val session: SessionDataStoreDataSource
) : ViewModel() {
    suspend fun login(account: User.Account): Result<User> {
        return authenticationRepository.login(account)
    }
}