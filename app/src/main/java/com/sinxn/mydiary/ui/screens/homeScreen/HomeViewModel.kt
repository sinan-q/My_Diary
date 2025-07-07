package com.sinxn.mydiary.ui.screens.homeScreen

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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
) : ViewModel() {

    private val _diaries = MutableStateFlow<List<Diary>>(emptyList())
    val diaries: StateFlow<List<Diary>> = _diaries.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Diary>>(emptyList())
    val searchResults: StateFlow<List<Diary>> = _searchResults.asStateFlow()

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

    fun searchDiaries(query: String) {
        if (query.isEmpty()) {
            _searchResults.value = emptyList()
            return
        }

        // Split the query by "&&" or "||"
        // This regex will split by "&&" or "||" and also trim whitespace around the tokens
        val tokens = query.split(Regex("\\s*(\\|\\||&&)\\s*")).filter { it.isNotBlank() }

        // Determine the operator if present (this is a simplified example)
        // For more complex logic with mixed operators, you'd need a more robust parsing strategy.
        val operator = if (query.contains("&&")) "AND" else if (query.contains("||")) "OR" else "OR" // Default to OR if no operator

        if (tokens.isEmpty()) {
            // Handle cases where the query might only contain operators or is otherwise invalid
            // You might want to show a toast or clear the results
            _searchResults.value = emptyList()
            toast("Invalid search query")
            return
        }

        viewModelScope.launch {
            // You'll need to adapt your repository and DAO to handle tokenized search
            _searchResults.value = diaryRepository.searchDiariesWithTokens(tokens, operator)
        }
    }
}