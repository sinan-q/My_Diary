package com.sinxn.mydiary.data.repository

import com.sinxn.mydiary.data.local.dao.DiaryDao
import com.sinxn.mydiary.data.local.entities.Diary
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor(
    private val diaryDao: DiaryDao
) {

    fun getAllDiaries(): Flow<List<Diary>> = diaryDao.getAllDiaries()

    suspend fun insertDiary(diary: Diary) {
        diaryDao.insertDiary(diary)
    }

    suspend fun deleteDiary(diary: Diary) {
        diaryDao.deleteDiary(diary)
    }

    suspend fun updateDiary(diary: Diary) {
        diaryDao.updateDiary(diary)
    }

    suspend fun getDiaryByTimestamp(timestamp: LocalDate): Diary? {
        return diaryDao.getDiaryByTimestamp(timestamp)
    }
}