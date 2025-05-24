package com.sinxn.mydiary.ui.screens.calenderScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.sinxn.mydiary.ui.components.MyTopAppBar
import com.sinxn.mydiary.ui.screens.diaryScreen.DiaryViewModel

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun CalenderScreen(
    diaryViewModel: DiaryViewModel,
    onBackup: () -> Unit,
    modifier: Modifier = Modifier
) {
    val diaries by diaryViewModel.diaries.collectAsState()

    Scaffold(
        topBar = {
            MyTopAppBar(onBackup)
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            CalendarGrid(diaries, onClick = {}, onMonthChange = {})
        }

    }
}