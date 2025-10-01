package com.sinxn.mydiary.ui.screens.backupScreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.sinxn.mydiary.data.local.entities.Diary
import com.sinxn.mydiary.data.repository.DiaryRepository
import com.sinxn.mydiary.utils.LocalDateAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val repository: DiaryRepository
) : ViewModel() {

    private val _backupState = MutableStateFlow<BackupState>(BackupState.Idle)
    val backupState: StateFlow<BackupState> = _backupState


    fun exportDatabase(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _backupState.value = BackupState.Loading // Indicate loading
            try {
                val diary = repository.getAllDiaries().first()
                val gson = GsonBuilder()
                    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
                    .setPrettyPrinting()
                    .create()

                // It's often cleaner to create a single wrapper object for your backup
                val backupData = mapOf(
                    "diary" to diary,
                )
                val jsonString = gson.toJson(backupData)

                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.writer().use { it.write(jsonString) } // Use writer for strings
                }
                _backupState.value = BackupState.Completed
            } catch (e: Exception) {
                e.printStackTrace()
                _backupState.value = BackupState.Error(e.localizedMessage ?: "Export failed")
            }
        }
    }

    data class BackupData(
        val diary: List<Diary>,
    )

    fun importDatabase(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _backupState.value = BackupState.Loading // Indicate loading
            try {
                val gson = GsonBuilder()
                    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
                    .create() // No need for pretty printing when reading

                val jsonString = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    InputStreamReader(inputStream).use { reader ->
                        reader.readText()
                    }
                }

                if (jsonString.isNullOrBlank()) {
                    _backupState.value = BackupState.Error("Backup file is empty or could not be read.")
                    return@launch
                }

                // Define the type for Gson to deserialize into.
                // This uses a helper data class BackupData.
                val backupDataType = object : TypeToken<BackupData>() {}.type
                val backupData: BackupData = gson.fromJson(jsonString, backupDataType)
                repository.clearAllDiaries()

                // Insert the imported data
                // Ensure your repositories have methods like these (e.g., insertAll, addEvents)
                repository.insertDiaries(backupData.diary)

                _backupState.value = BackupState.Completed
            } catch (e: Exception) {
                e.printStackTrace()
                _backupState.value = BackupState.Error(e.localizedMessage ?: "Import failed")
            }
        }
    }

    // Updated BackupState to include a loading state and error message
    sealed class BackupState {
        object Idle : BackupState()
        object Loading : BackupState() // Added for better UX
        object Completed : BackupState()
        data class Error(val message: String) : BackupState() // Can hold an error message
    }
}

