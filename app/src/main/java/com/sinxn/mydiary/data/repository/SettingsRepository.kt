package com.sinxn.mydiary.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sinxn.mydiary.ui.screens.lockScreen.LockState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


object PreferenceKeys { // Keep your keys organized
    val BIOMETRIC_AUTH_ENABLED = booleanPreferencesKey("biometric_auth_enabled")
    val DEFAULT_TITLE_ENABLED = booleanPreferencesKey("default_title_enabled")
    val DATE_FORMAT = stringPreferencesKey("date_format")
}

@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>

) {

    val isBiometricAuthEnabled: Flow<LockState> = dataStore.data
        .map { preferences ->
            if (preferences[PreferenceKeys.BIOMETRIC_AUTH_ENABLED] == true) LockState.LOCKED else LockState.UNLOCKED // Default to false
        }

    suspend fun setBiometricAuthEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[PreferenceKeys.BIOMETRIC_AUTH_ENABLED] = enabled
        }
    }

    val isDefaultTitleEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.DEFAULT_TITLE_ENABLED] == true
        }

    suspend fun setDefaultTitleEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[PreferenceKeys.DEFAULT_TITLE_ENABLED] = enabled
        }
    }

    val dateFormat: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.DATE_FORMAT] ?: "dd MMM ''yy • EEEE"
        }

    suspend fun setDateFormat(pattern: String) {
        dataStore.edit { settings ->
            settings[PreferenceKeys.DATE_FORMAT] = pattern
        }
    }
}
