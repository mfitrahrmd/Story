package com.mfitrahrmd.story.data.datasource.remote.dto

class LoginResponse(
    error: Boolean,
    message: String,
    val loginResult: LoginResult
) : BaseResponse(error, message) {
    class LoginResult(
        val userId: String,
        val name: String,
        val token: String
    )
}