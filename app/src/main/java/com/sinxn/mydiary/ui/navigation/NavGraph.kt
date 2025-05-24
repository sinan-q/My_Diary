package com.sinxn.mydiary.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sinxn.mydiary.ui.screens.calenderScreen.CalenderScreen
import com.sinxn.mydiary.ui.screens.diaryScreen.DiaryListScreen
import com.sinxn.mydiary.ui.screens.diaryScreen.DiaryViewModel
import com.sinxn.mydiary.ui.screens.diaryScreen.DiaryViewScreen
import com.sinxn.mydiary.utils.Converters
import com.sinxn.mytasks.ui.screens.backupScreen.BackupScreen
import com.sinxn.mytasks.ui.screens.backupScreen.BackupViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavGraph(
    navController: NavHostController,
    diaryViewModel: DiaryViewModel,
    backupViewModel: BackupViewModel,
    modifier: Modifier = Modifier
) {
    val onAddDiaryClick: () -> Unit = { navController.navigate("diary/add") }
    val onDiaryClick: (timestamp: LocalDate) -> Unit = { timestamp ->
        navController.navigate("diary/${timestamp.toEpochDay()}")
    }


    val onBack: () -> Unit = {
        navController.popBackStack()
    }

    val onBackup: () -> Unit = {
        navController.navigate("backup")
    }

    NavHost(
        navController = navController,
        startDestination = "diaryList",
        modifier = modifier
    ) {
        composable("diaryList") {
            DiaryListScreen(
                diaryViewModel = diaryViewModel,
                onAddDiaryClick = onAddDiaryClick,
                onDiaryClick = onDiaryClick
            )
        }
        composable(
            route = "diary/{timestamp}",
            arguments = listOf(
                navArgument("timestamp") { type = NavType.LongType; defaultValue = LocalDate.now().toEpochDay() },
                )
        ) { backStackEntry ->
            val timestamp = backStackEntry.arguments?.getLong("timestamp") ?: LocalDate.now().toEpochDay()
            DiaryViewScreen (
                timestamp = Converters().epochDayToLocalDate(timestamp),
                onFinish = { navController.popBackStack() },
                diaryViewModel = diaryViewModel,
                modifier = Modifier,
                isNew = false
            )
        }

        composable(route = "diary/add") {
            DiaryViewScreen (
                timestamp = LocalDate.now(),
                onFinish = { navController.popBackStack() },
                diaryViewModel = diaryViewModel,
                modifier = Modifier,
                isNew = true
            )
        }
        composable(route = "backup") {
            BackupScreen(viewModel = backupViewModel)
        }
        composable(route = "calender") {
            CalenderScreen(diaryViewModel = diaryViewModel)
        }
    }
}
