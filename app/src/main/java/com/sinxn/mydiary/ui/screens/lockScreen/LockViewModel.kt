package com.sinxn.mydiary.ui.screens.lockScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinxn.mydiary.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LockViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {

    val isBiometricAuthEnabled: StateFlow<LockState> =
        settingsRepository.isBiometricAuthEnabled
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = LockState.INITIAL // Or determine a better initial value, perhaps from a synchronous check if critical
            )
}

enum class LockState {
    LOCKED,
    UNLOCKED,
    INITIAL
}