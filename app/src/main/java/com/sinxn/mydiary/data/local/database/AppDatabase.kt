package com.sinxn.mydiary.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sinxn.mydiary.data.local.dao.DiaryDao
import com.sinxn.mydiary.data.local.entities.Diary
import com.sinxn.mydiary.utils.Converters

const val DB_VERSION = 1
@Database(entities = [Diary::class],
    version = DB_VERSION,
    exportSchema = true,
//    autoMigrations = [
//        AutoMigration(from = 1, to = DB_VERSION)
//    ]
    )
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
}