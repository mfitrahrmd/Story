package com.mfitrahrmd.story.data

sealed interface Result<out T : Any> {
    sealed class Success<T : Any>(val message: String) : Result<T> {
        class Message(message: String) : Success<Nothing>(message)
        class WithData<T : Any>(message: String, val data: T) : Success<T>(message)
    }

    sealed class Failed(val message: String, val code: String) : Result<Nothing> {
        class UnknownError(message: String, code: String = "0") : Failed(message, code)
        class NetworkError(message: String, code: String = "1") : Failed(message, code)
        class ApiError(message: String, code: String = "2") : Failed(message, code)
    }
}