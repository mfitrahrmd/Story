package com.mfitrahrmd.story.data.datasource.remote.services

import com.mfitrahrmd.story.data.datasource.remote.dto.BaseResponse
import com.mfitrahrmd.story.data.datasource.remote.dto.GetAllStoriesResponse
import com.mfitrahrmd.story.data.datasource.remote.dto.GetDetailStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryService {
    @Multipart
    @POST("stories/guest")
    suspend fun createStoryAsGuest(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ): Response<BaseResponse>

    @Multipart
    @POST("stories/guest")
    suspend fun createStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ): Response<BaseResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Boolean? = null
    ): Response<GetAllStoriesResponse>

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<GetDetailStoryResponse>
}