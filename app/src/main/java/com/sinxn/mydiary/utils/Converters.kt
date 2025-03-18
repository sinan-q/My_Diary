package com.sinxn.mydiary.utils

import androidx.room.TypeConverter
import java.time.LocalDate

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