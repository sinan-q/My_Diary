package com.sinxn.mydiary.ui.screens.diaryScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinxn.mydiary.data.local.entities.Diary
import com.sinxn.mydiary.data.repository.DiaryRepository
import com.sinxn.mydiary.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val isDefaultTitleEnabled: StateFlow<Boolean> =
        settingsRepository.isDefaultTitleEnabled
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = false
            )

    val dateFormat: StateFlow<String> =
        settingsRepository.dateFormat
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = "dd MMM ''yy • EEEE"
            )

    private val _diary = MutableStateFlow(getEmptyDiary())
    val diary: StateFlow<Diary> = _diary

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    init {
        viewModelScope.launch {
            isDefaultTitleEnabled.collect { enabled ->
                val currentDiary = _diary.value
                if (currentDiary.id == 0L) {
                    val dayName = currentDiary.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    if (currentDiary.title.isEmpty() && enabled) {
                        _diary.value = currentDiary.copy(title = dayName)
                    } else if (currentDiary.title == dayName && !enabled) {
                        _diary.value = currentDiary.copy(title = "")
                    }
                }
            }
        }
    }

    fun toast(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }

    fun setDate(date: LocalDate) {
        viewModelScope.launch {
            _diary.value = diaryRepository.getDiaryByDate(date) ?: getEmptyDiary(date)
        }
    }

    fun getEmptyDiary(date: LocalDate = LocalDate.now()): Diary {
        val title = if (isDefaultTitleEnabled.value) date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()) else ""
        return Diary(date = date, title = title)
    }

    fun addDiary() {
        viewModelScope.launch {
            try {
                val status = diaryRepository.insertDiary(diary.value)
                if (status == -1L) toast(DiaryConstants.DIARY_UPDATED)
                else {
                    _diary.value = _diary.value.copy(id = status)
                    toast(DiaryConstants.DIARY_ADDED)
                }
            }
            catch (e: Exception) {
                toast(e.message ?: DiaryConstants.UNKNOWN_ERROR)
            }
            }
    }

    fun resetDiary() {
        _diary.value = getEmptyDiary()
    }

    fun updateDiaryState(diary: Diary) {
        _diary.value = diary
    }

    fun deleteDiary() {
        viewModelScope.launch {
            val status = diaryRepository.deleteDiary(diary.value)
            toast(if (status == 0) DiaryConstants.DIARY_NOT_DELETED else DiaryConstants.DIARY_DELETED )
        }
    }

    fun fetchDiaryById(id : Long) {
        viewModelScope.launch {
            _diary.value = diaryRepository.getDiaryById(id) ?: getEmptyDiary()
        }
    }

}