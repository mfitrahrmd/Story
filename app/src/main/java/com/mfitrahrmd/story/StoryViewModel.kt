package com.mfitrahrmd.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.datasource.datastore.SessionDataStoreDataSource
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.repository.IStoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File

class StoryViewModel(
    private val storyRepository: IStoryRepository,
    val session: SessionDataStoreDataSource
) : ViewModel() {
    private val refreshTriggerChan = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class)
    val storyPagingDataFlow: Flow<PagingData<Story>> =
        refreshTriggerChan.receiveAsFlow().combine(session.getToken()) { _, token ->
            storyRepository.getStoryPages(token, null, null, null).cachedIn(viewModelScope)
        }.flatMapLatest { it }

    fun refresh() {
        viewModelScope.launch {
            refreshTriggerChan.send(Unit)
        }
    }

    suspend fun createStoryAsGuest(story: Story, imageFile: File): Result<Boolean> =
        storyRepository.createStory(story, imageFile)
}