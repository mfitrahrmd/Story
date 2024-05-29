package com.mfitrahrmd.story

import com.mfitrahrmd.story.data.repositories.IAuthenticationRepository

interface IApplicationContainer {
    val authenticationRepository: IAuthenticationRepository
}