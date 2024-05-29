package com.mfitrahrmd.story.data.datasource.remote

import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.datasource.IStoryDataSource
import com.mfitrahrmd.story.data.datasource.remote.services.NetworkResponse
import com.mfitrahrmd.story.data.datasource.remote.services.RemoteService
import com.mfitrahrmd.story.data.entities.Story
import com.mfitrahrmd.story.data.entities.User
import com.mfitrahrmd.story.data.entities.remote.RemoteStory
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRemoteDataSource(
    private val remoteService: RemoteService
) : IStoryDataSource {
    override suspend fun createStory(story: Story, photo: File): Result<Boolean> {
        val requestFile = photo.asRequestBody("image/jpeg".toMediaType())
        val descriptionBody = story.description.toRequestBody("text/plain".toMediaType())
        val latBody = story.lat.toString().toRequestBody("text/plain".toMediaType())
        val lonBody = story.lon.toString().toRequestBody("text/plain".toMediaType())
        val file = MultipartBody.Part.createFormData("photo", photo.name, requestFile)
        val result = remoteService.storyService.createStoryAsGuest(file, descriptionBody, latBody, lonBody)
        return when (result) {
            is NetworkResponse.ApiError -> Result.Failed(result.body.message)
            is NetworkResponse.NetworkError -> Result.Failed(result.error.message.orEmpty())
            is NetworkResponse.Success -> Result.Success(true)
            is NetworkResponse.UnknownError -> Result.Failed(result.error?.message.orEmpty())
        }
    }

    override suspend fun createStory(token: String, story: Story, photo: File): Result<Boolean> {
        val requestFile = photo.asRequestBody("image/jpeg".toMediaType())
        val descriptionBody = story.description.toRequestBody("text/plain".toMediaType())
        val latBody = story.lat.toString().toRequestBody("text/plain".toMediaType())
        val lonBody = story.lon.toString().toRequestBody("text/plain".toMediaType())
        val file = MultipartBody.Part.createFormData("photo", photo.name, requestFile)
        val result = remoteService.storyService.createStory(token, file, descriptionBody, latBody, lonBody)
        return when (result) {
            is NetworkResponse.ApiError -> Result.Failed(result.body.message)
            is NetworkResponse.NetworkError -> Result.Failed(result.error.message.orEmpty())
            is NetworkResponse.Success -> Result.Success(true)
            is NetworkResponse.UnknownError -> Result.Failed(result.error?.message.orEmpty())
        }
    }

    override suspend fun getAllStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Boolean?
    ): Result<List<RemoteStory>> {
        val result = remoteService.storyService.getAllStories(token, page, size, location)
        return when (result) {
            is NetworkResponse.ApiError -> Result.Failed(result.body.message)
            is NetworkResponse.NetworkError -> Result.Failed(result.error.message.orEmpty())
            is NetworkResponse.Success -> Result.Success(result.body.listStory)
            is NetworkResponse.UnknownError -> Result.Failed(result.error?.message.orEmpty())
        }
    }

    override suspend fun getDetailStory(token: String, storyId: String): Result<RemoteStory> {
        val result = remoteService.storyService.getDetailStory(token, storyId)
        return when (result) {
            is NetworkResponse.ApiError -> Result.Failed(result.body.message)
            is NetworkResponse.NetworkError -> Result.Failed(result.error.message.orEmpty())
            is NetworkResponse.Success -> Result.Success(result.body.story)
            is NetworkResponse.UnknownError -> Result.Failed(result.error?.message.orEmpty())
        }
    }
}