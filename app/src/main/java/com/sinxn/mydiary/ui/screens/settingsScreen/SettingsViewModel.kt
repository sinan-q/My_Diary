package com.sinxn.mydiary.ui.screens.settingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinxn.mydiary.data.repository.SettingsRepository
import com.sinxn.mydiary.ui.screens.lockScreen.LockState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val isBiometricAuthEnabled: StateFlow<LockState> =
        settingsRepository.isBiometricAuthEnabled
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000), // Keep active for 5s after last subscriber
                initialValue = LockState.INITIAL // Or determine a better initial value, perhaps from a synchronous check if critical
            )
    fun setBiometricAuthEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setBiometricAuthEnabled(enabled)
        }

    }

}