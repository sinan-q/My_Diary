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
    val onAddDiaryClick: () -> Unit = { navController.navigate("${Routes.DIARY_SCREEN}/add") }
    val onDiaryClick: (id: Long) -> Unit = { id ->
        navController.navigate("${Routes.DIARY_SCREEN}/id=${id}")
    }
    val onDayClick: (date: LocalDate) -> Unit = { date ->
        navController.navigate("${Routes.DIARY_SCREEN}/date=${date.toEpochDay()}")
    }

    val onBack: () -> Unit = {
        navController.popBackStack()
    }

    val onBackup: () -> Unit = {
        navController.navigate("backup")
    }

    NavHost(
        navController = navController,
        startDestination = Routes.DIARY_LIST_SCREEN,
        modifier = modifier
    ) {
        composable(Routes.DIARY_LIST_SCREEN) {
            DiaryListScreen(
                diaryViewModel = diaryViewModel,
                onAddDiaryClick = onAddDiaryClick,
                onDiaryClick = onDiaryClick,
                onBackup = onBackup
            )
        }
        composable(
            route = "${Routes.DIARY_SCREEN}/date={timestamp}",
            arguments = listOf(
                navArgument("timestamp") { type = NavType.LongType; defaultValue = LocalDate.now().toEpochDay() },
                )
        ) { backStackEntry ->
            val timestamp = backStackEntry.arguments?.getLong("timestamp") ?: LocalDate.now().toEpochDay()
            DiaryViewScreen (
                date = Converters().epochDayToLocalDate(timestamp),
                onFinish = { navController.popBackStack() },
                diaryViewModel = diaryViewModel,
                modifier = Modifier,
            )
        }
        composable(
            route = "${Routes.DIARY_SCREEN}/id={id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType; defaultValue = 0 },
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0
            DiaryViewScreen (
                id = id,
                onFinish = { navController.popBackStack() },
                diaryViewModel = diaryViewModel,
                modifier = Modifier,
            )
        }

        composable(route = "${Routes.DIARY_SCREEN}/add") {
            DiaryViewScreen (
                onFinish = { navController.popBackStack() },
                diaryViewModel = diaryViewModel,
                modifier = Modifier,
            )
        }
        composable(route = Routes.BACKUP_SCREEN) {
            BackupScreen(viewModel = backupViewModel)
        }
        composable(route = Routes.CALENDER_SCREEN) {
            CalenderScreen(diaryViewModel = diaryViewModel, onBackup = onBackup)
        }
    }
}
