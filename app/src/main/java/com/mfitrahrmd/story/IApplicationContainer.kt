package com.mfitrahrmd.story

import com.mfitrahrmd.story.data.repository.IAuthenticationRepository

interface IApplicationContainer {
    val authenticationRepository: IAuthenticationRepository
}