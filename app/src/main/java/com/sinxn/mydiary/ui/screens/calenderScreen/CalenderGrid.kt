package com.sinxn.mydiary.ui.screens.calenderScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sinxn.mydiary.data.local.entities.Diary
import com.sinxn.mydiary.ui.components.RectangleCard
import java.time.LocalDate
import kotlin.math.ceil

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun CalendarGrid(
    diaries: List<Diary>,
    onClick: (LocalDate) -> Unit,
    onMonthChange: (LocalDate) -> Unit,
) {
    var localDate by remember { mutableStateOf(LocalDate.now())  }
    val WEEK_DAYS = listOf( "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    val firstDayOfMonth = localDate.withDayOfMonth(1)
    val firstDayOfWeek = firstDayOfMonth.minusDays(firstDayOfMonth.dayOfWeek.value.toLong())
    val lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1)
    val lastDayOfWeek = lastDayOfMonth.plusDays(7 - lastDayOfMonth.dayOfWeek.value.toLong())
    val days = firstDayOfWeek.datesUntil(lastDayOfWeek).toList()
    val totalColumns = ceil(days.size / 7f).toInt()

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        localDate = localDate.minusMonths(1)
                        onMonthChange(localDate)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Month"
                    )
                }
                Text(text = localDate.month.toString())
                IconButton(
                    onClick = {
                        localDate = localDate.plusMonths(1)
                        onMonthChange(localDate)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next Month"
                    )
                }
            }

        }
        Row {
           for (day in WEEK_DAYS) {
               CalendarDayWeekItem(modifier = Modifier.weight(1f), day = day)
           }
        }
        for (x in 0 until totalColumns) {
            Row(Modifier.fillMaxWidth()) {
                for (y in x*7 until x*7+7) {
                    val targetDay = days[y]
                    val diaryForTheDay = diaries.find { it.date == targetDay }
                    CalendarDayItem(modifier = Modifier.weight(1f), day = targetDay, isDiaryExistsForTheDay = diaryForTheDay != null, onClick = onClick)
                }

            }
        }

    }
}

@Composable
fun CalendarDayItem(modifier: Modifier, day: LocalDate, isDiaryExistsForTheDay: Boolean, onClick: (LocalDate) -> Unit = {}) {
    RectangleCard(
        modifier = modifier.height(80.dp),
        onClick = { onClick(day) },
        colors = CardDefaults.cardColors(
            containerColor = if (day == LocalDate.now()) Color.Unspecified else Color.Transparent
        )
    ) {
        Column(modifier = Modifier.padding(4.dp).fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = day.dayOfMonth.toString(), style = MaterialTheme.typography.bodyLarge , fontWeight = if(isDiaryExistsForTheDay) FontWeight.ExtraBold else FontWeight.Light)
        }
    }
}

@Composable
fun CalendarDayWeekItem(modifier: Modifier, day: String) {
    RectangleCard (modifier = modifier) {
        Column(modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = day)
        }
    }
}
