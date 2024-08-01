package com.mfitrahrmd.story.data.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.mfitrahrmd.story.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageProvider {
    private const val directoryName = "Story/"
    private const val fileExtension = ".jpg"
    private val fileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

    fun createImageFile(context: Context): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName$fileExtension")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/$directoryName")
            }

            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        } else {
            val imageFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/$directoryName$fileName$fileExtension")
            if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()

            FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.fileprovider", imageFile)
        }
    }

    fun uriToImageFile(imageUri: Uri, context: Context): File {
        val imageFile = createCustomTempFile(context)
        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            imageFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return imageFile
    }

    private fun createCustomTempFile(context: Context): File {
        return File.createTempFile(fileName, fileExtension, context.externalCacheDir)
    }
}