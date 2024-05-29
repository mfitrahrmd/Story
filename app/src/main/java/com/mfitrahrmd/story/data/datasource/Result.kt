package com.mfitrahrmd.story.data.datasource

sealed interface Result<out T : Any> {
    data class Success<T : Any>(val data: T) : Result<T>
    data class Failed(val message: String) : Result<Nothing>
}