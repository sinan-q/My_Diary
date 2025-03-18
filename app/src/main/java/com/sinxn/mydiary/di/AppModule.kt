package com.sinxn.mydiary.di

import android.content.Context
import androidx.room.Room
import com.sinxn.mydiary.data.local.database.AppDatabase
import com.sinxn.mydiary.data.repository.DiaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDiaryDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDiaryDao(appDatabase: AppDatabase) = appDatabase.diaryDao()

    @Provides
    @Singleton
    fun provideDiaryRepository(appDatabase: AppDatabase): DiaryRepository {
        return DiaryRepository(appDatabase.diaryDao())
    }

}