package com.sinxn.mydiary.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sinxn.mydiary.data.local.entities.Diary
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DiaryDao {

    @Query("SELECT * FROM diary ORDER BY timestamp DESC")
    fun getAllDiaries(): Flow<List<Diary>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDiary(diary: Diary)

    @Delete
    suspend fun deleteDiary(diary: Diary)

    @Update
    suspend fun updateDiary(diary: Diary)


    @Query("SELECT * FROM diary WHERE timestamp = :timestamp")
    suspend fun getDiaryByTimestamp(timestamp: LocalDate): Diary?

}