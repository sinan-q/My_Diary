package com.sinxn.mydiary.utils


import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Extension function for formatting Date
fun LocalDate.formatDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM ''yy", Locale.getDefault())
    return this.format(formatter)
}

fun fromMillis(millis: Long): LocalDate {
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
}

fun LocalDate.toMillis(): Long {
    return LocalDateTime.of(this, LocalTime.now()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}