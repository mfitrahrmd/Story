package com.mfitrahrmd.story.data.util

import android.net.Uri
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface IFileProvider {
    fun uriToJpgFile(uri: Uri): File
}