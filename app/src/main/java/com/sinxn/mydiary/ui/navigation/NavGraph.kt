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
import com.sinxn.mydiary.ui.screens.aboutScreen.AboutScreen
import com.sinxn.mydiary.ui.screens.backupScreen.BackupScreen
import com.sinxn.mydiary.ui.screens.calenderScreen.CalenderScreen
import com.sinxn.mydiary.ui.screens.diaryScreen.DiaryViewScreen
import com.sinxn.mydiary.ui.screens.homeScreen.DiaryListScreen
import com.sinxn.mydiary.ui.screens.homeScreen.HomeViewModel
import com.sinxn.mydiary.ui.screens.lockScreen.LockScreen
import com.sinxn.mydiary.ui.screens.settingsScreen.SettingsScreen
import com.sinxn.mydiary.utils.Converters
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {


    NavHost(
        navController = navController,
        startDestination = Screen.Lock.route,
        modifier = modifier
    ) {
        composable(Screen.Lock.route) {
            LockScreen {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Lock.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Screen.Home.route) {
            DiaryListScreen(
                homeViewModel = homeViewModel,
                navController = navController
            )
        }
        composable(
            route = Screen.DiaryView.ByDate.route,
            arguments = listOf(navArgument("timestamp") { type = NavType.LongType; defaultValue = LocalDate.now().toEpochDay() },)
        ) { backStackEntry ->
            val timestamp = backStackEntry.arguments?.getLong("timestamp") ?: LocalDate.now().toEpochDay()
            DiaryViewScreen (
                date = Converters().epochDayToLocalDate(timestamp),
                onFinish = { navController.popBackStack() },
            )
        }
        composable(
            route = Screen.DiaryView.ById.route,
            arguments = listOf(
                navArgument("id") { type = NavType.LongType; defaultValue = 0 },
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0
            DiaryViewScreen (
                id = id,
                onFinish = { navController.popBackStack() }
            )
        }

        composable(route = Screen.DiaryView.AddDiary.route) {
            DiaryViewScreen { navController.popBackStack() }
        }
        composable(route = Screen.Calender.route) {
            CalenderScreen(homeViewModel = homeViewModel, navController = navController, onClick = { date ->
                navController.navigate(Screen.DiaryView.ByDate.createRoute(Converters().localDateToEpochDay(date)))
            })
        }
        composable(route = Screen.Backup.route) { BackupScreen() }
        composable(route = Screen.Settings.route) { SettingsScreen() }
        composable(route = Screen.About.route) { AboutScreen() }
    }
}
