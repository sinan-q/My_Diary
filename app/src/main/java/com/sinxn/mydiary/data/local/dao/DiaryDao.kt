package com.sinxn.mydiary.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.sinxn.mydiary.data.local.entities.Diary
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DiaryDao {

    @Query("SELECT * FROM diary ORDER BY date DESC")
    fun getAllDiaries(): Flow<List<Diary>>

    @Upsert
    suspend fun insertDiary(diary: Diary): Long

    @Delete
    suspend fun deleteDiary(diary: Diary): Int

    @Query("SELECT * FROM diary WHERE date = :date")
    suspend fun getDiaryByDate(date: LocalDate): Diary?

    @Query("SELECT * FROM diary WHERE id = :id")
    suspend fun getDiaryById(id: Long): Diary?

    @Query("SELECT * FROM diary WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    suspend fun searchDiaries(query: String): List<Diary>
}