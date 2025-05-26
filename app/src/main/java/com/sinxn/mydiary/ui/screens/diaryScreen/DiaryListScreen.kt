package com.sinxn.mydiary.ui.screens.diaryScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sinxn.mydiary.ui.components.MyTopAppBar
import com.sinxn.mydiary.ui.components.RectangleFAB

@Composable
fun DiaryListScreen(
    diaryViewModel: DiaryViewModel,
    onAddDiaryClick: () -> Unit,
    onDiaryClick: (id: Long) -> Unit,
    onBackup: () -> Unit,
    modifier: Modifier = Modifier
) {
    val diaries by diaryViewModel.diaries.collectAsState()
    Scaffold(
        floatingActionButton = {
            RectangleFAB(onClick = { onAddDiaryClick() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Diary")
            }
        },
        topBar = {
            MyTopAppBar(onBackup)
        },
    ) { paddingValues ->
        if(diaries.isEmpty())
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No Diaries Found", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }

        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            items(diaries) { diary ->
                DiaryItem(
                    diary = diary,
                    onClick = { onDiaryClick(diary.id) }
                )
            }
        }
    }
}