package com.mfitrahrmd.story.data.util

import android.net.Uri
import java.io.File

interface IFileProvider {
    fun uriToJpgFile(uri: Uri): File
}