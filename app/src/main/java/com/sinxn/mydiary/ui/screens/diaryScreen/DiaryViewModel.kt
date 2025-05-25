package com.sinxn.mydiary.ui.screens.diaryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinxn.mydiary.data.local.entities.Diary
import com.sinxn.mydiary.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
) : ViewModel() {

    private val _diaries = MutableStateFlow<List<Diary>>(emptyList())
    val diaries: StateFlow<List<Diary>> = _diaries.asStateFlow()

    private val _diary = MutableStateFlow<Diary?>(null)
    val diary: StateFlow<Diary?> = _diary

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun toast(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }
    init {
        viewModelScope.launch {
            diaryRepository.getAllDiaries().collect { diaryList ->
                _diaries.value = diaryList
            }
        }
    }

    fun addDiary(diary: Diary) {
        viewModelScope.launch {
            val existingDiary = diaryRepository.getDiaryByTimestamp(timestamp = diary.timestamp)
            if(existingDiary == null) {
                diaryRepository.insertDiary(diary)
                toast("Diary added successfully")
                } else {
                toast("Diary already exists")
            }
        }
    }

    fun deleteDiary(diary: Diary) {
        viewModelScope.launch {
            diaryRepository.deleteDiary(diary)
            toast("Note Deleted")
        }
    }

    suspend fun fetchDiaryByTimestamp(timestamp:LocalDate): Diary? {
           return diaryRepository.getDiaryByTimestamp(timestamp)

    }
}