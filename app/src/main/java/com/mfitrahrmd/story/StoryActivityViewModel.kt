package com.mfitrahrmd.story

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.datasource.datastore.SessionDataStoreDataSource
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.repository.IStoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import java.io.File

class StoryActivityViewModel(
    private val storyRepository: IStoryRepository,
    val session: SessionDataStoreDataSource
) : ViewModel() {
    private val _storyPagingDataFlow: MutableStateFlow<PagingData<Story>> = MutableStateFlow(PagingData.empty())
    val storyPagingDataFlow: StateFlow<PagingData<Story>>
        get() = _storyPagingDataFlow

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            session.getToken().collectLatest { token ->
                val storyPaging = storyRepository.getStoryPages(token, null, null, null).cachedIn(viewModelScope)
                _storyPagingDataFlow.emitAll(storyPaging)
            }
        }
    }

    suspend fun createStoryAsGuest(story: Story, imageFile: File): Result<Boolean> =
        storyRepository.createStory(story, imageFile)
}