package com.sinxn.mydiary.ui.screens.calenderScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.sinxn.mydiary.ui.components.BottomBar
import com.sinxn.mydiary.ui.components.MyTopAppBar
import com.sinxn.mydiary.ui.screens.diaryScreen.DiaryViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun CalenderScreen(
    diaryViewModel: DiaryViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onClick: (LocalDate) -> Unit
) {
    val diaries by diaryViewModel.diaries.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets.safeContent,
        bottomBar = { BottomBar(navController = navController) },
        topBar = {
            MyTopAppBar(navController = navController)
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            CalendarGrid(diaries, onClick = onClick, onMonthChange = {})
        }

    }
}