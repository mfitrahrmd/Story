package com.mfitrahrmd.story

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            StoryActivityViewModel(
                storyApplication().applicationContainer.storyRepository,
                storyApplication().applicationContainer.session,
            )
        }
        initializer {
            MainActivityViewModel(
                storyApplication().applicationContainer.authenticationRepository,
                storyApplication().applicationContainer.session,
            )
        }
        initializer {
            DetailStoryViewModel(
                storyApplication().applicationContainer.storyRepository,
                storyApplication().applicationContainer.session,
            )
        }
    }
}

fun CreationExtras.storyApplication(): StoryApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StoryApplication)