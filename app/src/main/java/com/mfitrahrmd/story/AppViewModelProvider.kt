package com.mfitrahrmd.story

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            StoryViewModel(
                storyApplication().applicationContainer.storyRepository,
                storyApplication().applicationContainer.session,
            )
        }
        initializer {
            MainViewModel(
                storyApplication().applicationContainer.authenticationRepository,
                storyApplication().applicationContainer.session,
            )
        }
    }
}

fun CreationExtras.storyApplication(): StoryApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StoryApplication)