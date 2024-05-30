package com.mfitrahrmd.story.data.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileProvider(
    private val context: Context,
    val path: String
) : IFileProvider {
    private fun createCustomTempFile(
        filePrefix: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()),
        fileName: String? = null,
        fileExtension: String
    ): File {
        val fileDir = File(context.externalCacheDir, path)
        if (!fileDir.exists()) {
            fileDir.mkdir()
        }
        return File.createTempFile("$filePrefix$fileName", fileExtension, fileDir)
    }

    private fun uriToFile(uri: Uri, fileExtension: String): File {
        val file = createCustomTempFile(
            fileExtension = fileExtension
        )
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val outputStream = file.outputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        outputStream.close()
        inputStream.close()

        return file
    }

    override fun uriToJpgFile(uri: Uri): File {
        return uriToFile(uri, ".jpg")
    }
}