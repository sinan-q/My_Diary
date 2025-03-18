package com.sinxn.mytasks.ui.screens.backupScreen

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

fun uriToFile(uri: Uri, context: Context): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File(context.cacheDir, "backup${LocalDateTime.now()}.db")

    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}