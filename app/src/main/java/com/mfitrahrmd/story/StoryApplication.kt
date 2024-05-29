package com.mfitrahrmd.story

import android.app.Application

class StoryApplication : Application() {
    val applicationContainer: IApplicationContainer = ApplicationContainer(this)
}