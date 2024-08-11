package com.mfitrahrmd.story

sealed interface UiState<out T : Any> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Finish<T : Any>(val result: T): UiState<T>
}