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

    suspend fun insertDiary(diary: Diary): Long = diaryDao.insertDiary(diary)


    suspend fun deleteDiary(diary: Diary): Int = diaryDao.deleteDiary(diary)

    suspend fun getDiaryByDate(date: LocalDate): Diary? = diaryDao.getDiaryByDate(date)

    suspend fun getDiaryById(id: Long): Diary? = diaryDao.getDiaryById(id)

    suspend fun searchDiaries(query: String): List<Diary> = diaryDao.searchDiaries(query)
}