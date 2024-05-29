package com.mfitrahrmd.story.data.datasource.remote.services

import com.mfitrahrmd.story.data.datasource.remote.dto.BaseResponse
import com.mfitrahrmd.story.data.datasource.remote.dto.LoginRequest
import com.mfitrahrmd.story.data.datasource.remote.dto.LoginResponse
import com.mfitrahrmd.story.data.datasource.remote.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("register")
    suspend fun register(
        @Body body: RegisterRequest
    ): NetworkResponse<BaseResponse, BaseResponse>

    @POST("login")
    suspend fun login(
        @Body body: LoginRequest
    ): NetworkResponse<LoginResponse, BaseResponse>
}