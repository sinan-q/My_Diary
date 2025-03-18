package com.sinxn.mydiary.ui.screens.diaryScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sinxn.mydiary.data.local.entities.Diary
import com.sinxn.mytasks.ui.components.RectangleFAB
import java.time.LocalDate

@Composable
fun DiaryListScreen(
    diaries: List<Diary>,
    onAddDiaryClick: () -> Unit,
    onDiaryClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            RectangleFAB(onClick = { onAddDiaryClick() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Diary")
            }
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        ) {
            items(diaries) { diary ->
                DiaryItem(
                    diary = diary,
                    onClick = { onDiaryClick(diary.timestamp) }
                )
            }
        }
    }
}