package com.mfitrahrmd.story.data.mapper

import com.mfitrahrmd.story.data.datasource.remote.dto.LoginRequest
import com.mfitrahrmd.story.data.datasource.remote.dto.RegisterRequest
import com.mfitrahrmd.story.data.entity.User

fun User.toRegisterRequest(): RegisterRequest {
    if (this.account == null) {
        return RegisterRequest(this.name, "", "")
    }

    return RegisterRequest(this.name, this.account.email, this.account.password)
}

fun User.Account.toLoginRequest(): LoginRequest {
    return LoginRequest(this.email, this.password)
}