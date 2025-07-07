package com.sinxn.mydiary.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sinxn.mydiary.ui.screens.backupScreen.BackupScreen
import com.sinxn.mydiary.ui.screens.backupScreen.BackupViewModel
import com.sinxn.mydiary.ui.screens.calenderScreen.CalenderScreen
import com.sinxn.mydiary.ui.screens.diaryScreen.DiaryViewModel
import com.sinxn.mydiary.ui.screens.diaryScreen.DiaryViewScreen
import com.sinxn.mydiary.ui.screens.homeScreen.DiaryListScreen
import com.sinxn.mydiary.ui.screens.homeScreen.HomeViewModel
import com.sinxn.mydiary.utils.Converters
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    diaryViewModel: DiaryViewModel = hiltViewModel(),
) {
    val onAddDiaryClick: () -> Unit = { navController.navigate("${Routes.DIARY_SCREEN}/add") }
    val onDiaryClick: (id: Long) -> Unit = { id ->
        navController.navigate("${Routes.DIARY_SCREEN}/id=${id}")
    }

    NavHost(
        navController = navController,
        startDestination = Routes.DIARY_LIST_SCREEN,
        modifier = modifier
    ) {
        composable(Routes.DIARY_LIST_SCREEN) {
            DiaryListScreen(
                homeViewModel = homeViewModel,
                onAddDiaryClick = onAddDiaryClick,
                onDiaryClick = onDiaryClick,
                navController = navController
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
            BackupScreen()
        }
        composable(route = Routes.CALENDER_SCREEN) {
            CalenderScreen(homeViewModel = homeViewModel, navController = navController, onClick = { date ->
                navController.navigate("${Routes.DIARY_SCREEN}/date=${Converters().localDateToEpochDay(date)}")
            })
        }
    }
}
