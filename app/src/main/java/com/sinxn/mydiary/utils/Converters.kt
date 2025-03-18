package com.sinxn.mydiary.utils

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {

    @TypeConverter
    fun epochDayToLocalDate(value: Long): LocalDate {
        return LocalDate.ofEpochDay(value)
    }

    @TypeConverter
    fun localDateToEpochDay(date: LocalDate): Long {
        return date.toEpochDay()
    }
}