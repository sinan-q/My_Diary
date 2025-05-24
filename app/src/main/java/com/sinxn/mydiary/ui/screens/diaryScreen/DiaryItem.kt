package com.sinxn.mydiary.ui.screens.diaryScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinxn.mydiary.data.local.entities.Diary
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DiaryItem(
    diary: Diary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row() {
        Column(modifier = Modifier.fillMaxHeight().padding(end = 10.dp, top= 15.dp), verticalArrangement = Arrangement.Center) {
            Text(diary.timestamp.dayOfMonth.toString(), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Text(diary.timestamp.month.getDisplayName(TextStyle.SHORT, Locale.getDefault()), fontSize = 12.sp, fontWeight = FontWeight.W400, lineHeight = 12.sp)
            Text(diary.timestamp.year.toString(), fontSize = 12.sp, fontWeight = FontWeight.W300, lineHeight = 10.sp)
        }
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.extraSmall)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = diary.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = diary.content,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

}
