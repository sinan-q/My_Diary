package com.sinxn.mydiary.ui.screens.backupScreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinxn.mydiary.data.local.database.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val database: AppDatabase,
) : ViewModel() {

    private val _backupState = MutableStateFlow(BackupState.Idle)
    val backupState : StateFlow<BackupState> = _backupState

    fun exportDatabase(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            database.close()
            val dbFile = context.getDatabasePath("database")
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    dbFile.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                _backupState.value = BackupState.Completed
            } catch (e: Exception) {
                e.printStackTrace()
                _backupState.value = BackupState.Error
            }
        }
    }

    fun importDatabase(context: Context, backupFile: File) {
        viewModelScope.launch(Dispatchers.IO) {
            database.close()
            val dbFile = context.getDatabasePath("database")
            try {
                backupFile.copyTo(dbFile, overwrite = true)
                _backupState.value = BackupState.Completed
            } catch (e: Exception) {
                e.printStackTrace()
                _backupState.value = BackupState.Error
            }
        }
    }

    enum class BackupState {
        Idle,
        Completed,
        Error
    }
}

