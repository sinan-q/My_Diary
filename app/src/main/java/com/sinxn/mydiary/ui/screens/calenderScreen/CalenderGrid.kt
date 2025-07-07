package com.sinxn.mydiary.ui.screens.calenderScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinxn.mydiary.data.local.entities.Diary
import com.sinxn.mydiary.ui.components.RectangleCard
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.ceil

@Composable
fun CalendarGrid(
    diaries: List<Diary>, // Diaries for THIS specific month
    displayMonth: YearMonth, // The month this grid instance should display
    onClick: (LocalDate) -> Unit,
    // No more onMonthChange needed here, as pager handles month changes
) {
    val WEEK_DAYS = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    val firstDayOfMonth = displayMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.minusDays(firstDayOfMonth.dayOfWeek.value.toLong() % 7)

    val lastDayOfMonth = displayMonth.atEndOfMonth()
    val lastDayOfWeek = lastDayOfMonth.plusDays((6 - (lastDayOfMonth.dayOfWeek.value % 7)).toLong())

    val days = mutableListOf<LocalDate>()
    var tempDate = firstDayOfWeek
    while (!tempDate.isAfter(lastDayOfWeek)) {
        days.add(tempDate)
        tempDate = tempDate.plusDays(1)
    }
    val totalRows = ceil(days.size / 7f).toInt()

    Column(
        Modifier
            .fillMaxSize() // Grid should fill the pager item
            .padding(horizontal = 8.dp), // Add some padding if needed
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Weekday Headers
        Row(Modifier.fillMaxWidth()) {
            for (day in WEEK_DAYS) {
                CalendarDayWeekItem(modifier = Modifier.weight(1f), day = day)
            }
        }
        // Days
        for (x in 0 until totalRows) {
            Row(Modifier.fillMaxWidth()) {
                for (y in 0 until 7) {
                    val dayIndex = x * 7 + y
                    if (dayIndex < days.size) {
                        val targetDay = days[dayIndex]
                        val diaryForTheDay = diaries.find { it.date == targetDay }
                        // Day is part of the displayMonth if its YearMonth matches
                        val isCurrentDisplayMonth = YearMonth.from(targetDay) == displayMonth

                        CalendarDayItem(
                            modifier = Modifier.weight(1f),
                            day = targetDay,
                            isCurrentDisplayMonth = isCurrentDisplayMonth,
                            isDiaryExistsForTheDay = diaryForTheDay != null,
                            onClick = onClick
                        )
                    } else {
                        Box(modifier = Modifier.weight(1f).height(60.dp)) {} // Spacer
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDayItem(
    modifier: Modifier,
    day: LocalDate,
    isCurrentDisplayMonth: Boolean,
    isDiaryExistsForTheDay: Boolean,
    onClick: (LocalDate) -> Unit = {}
) {
    val dayTextColor = if (isCurrentDisplayMonth) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f) // Standard disabled alpha
    }
    val containerColor = if (day == LocalDate.now() && isCurrentDisplayMonth) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }

    Card(
        modifier = modifier.height(60.dp),
        onClick = {
            if (isCurrentDisplayMonth) { // Only allow click if it's a day of the current month
                onClick(day)
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        enabled = isCurrentDisplayMonth // This will also affect the visual state (e.g. ripple)
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isDiaryExistsForTheDay && isCurrentDisplayMonth) FontWeight.ExtraBold else FontWeight.Light, // Only bold if in current month
                color = dayTextColor
            )
            if (isDiaryExistsForTheDay && isCurrentDisplayMonth) {
                Text(
                    text = ".",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.offset(y = 10.dp),
                    color = dayTextColor // Use same color as the day number
                )
            }
        }
    }
}


@Composable
fun CalendarDayWeekItem(modifier: Modifier, day: String) {
    RectangleCard (modifier = modifier) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp), // Added a bit more vertical padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = day, style = MaterialTheme.typography.bodySmall)
        }
    }
}
