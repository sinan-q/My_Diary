package com.sinxn.mydiary.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "diary")
data class Diary(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val date: LocalDate = LocalDate.now(),
    val title: String = "",
    val content: String = "",
)
