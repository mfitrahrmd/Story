package com.mfitrahrmd.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.datasource.datastore.SessionDataStoreDataSource
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.repository.IStoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailStoryViewModel(
    private val storyRepository: IStoryRepository,
    val session: SessionDataStoreDataSource
) : ViewModel() {
    private val _detailStoryFlow: MutableStateFlow<UiState<Result<Story>>> = MutableStateFlow(UiState.Idle)
    val detailStoryFlow: StateFlow<UiState<Result<Story>>>
        get() = _detailStoryFlow

    fun refresh(id: String) {
        viewModelScope.launch {
            session.getToken().collect { token ->
                val detailStory = storyRepository.getDetailStory(token, id)
                _detailStoryFlow.emit(UiState.Finish(detailStory))
            }
        }
    }
}