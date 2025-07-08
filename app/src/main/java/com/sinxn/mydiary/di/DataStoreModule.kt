package com.sinxn.mydiary.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


private const val USER_PREFERENCES_NAME = "settings"

// This is the extension property that Hilt will use to create the DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return appContext.dataStore // Uses the extension property
    }

}