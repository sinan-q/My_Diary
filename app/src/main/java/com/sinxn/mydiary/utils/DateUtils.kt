package com.sinxn.mydiary.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Extension function for formatting Date
fun LocalDate.formatDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    return this.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun fromMillis(millis :Long): LocalDate {
    return LocalDate.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
}

fun LocalDate.toMillis(): Long {
    return LocalDateTime.of(this, LocalTime.now()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}