package com.sinxn.mydiary

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sinxn.mydiary.ui.components.BottomBar
import com.sinxn.mydiary.ui.navigation.NavGraph
import com.sinxn.mydiary.ui.screens.diaryScreen.DiaryViewModel
import com.sinxn.mydiary.ui.screens.lockScreen.LockScreen
import com.sinxn.mydiary.ui.theme.MyDiaryTheme
import com.sinxn.mytasks.ui.screens.backupScreen.BackupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val backupViewModel: BackupViewModel by viewModels()
    private val diaryViewModel: DiaryViewModel by viewModels()
    private var isAuthenticated by mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            if (isAuthenticated) {
                MainAppContent(navController,diaryViewModel, backupViewModel)
            } else {
                LockScreen{ isAuthenticated = true }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun MainAppContent(navController: NavHostController, diaryViewModel: DiaryViewModel, backupViewModel: BackupViewModel) {
    MyDiaryTheme {
        Scaffold(
            contentWindowInsets = WindowInsets.safeContent,
            bottomBar = { BottomBar(navController = navController) },
            modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavGraph(
                navController = navController,
                diaryViewModel = diaryViewModel,
                modifier = Modifier.padding(innerPadding),
                backupViewModel = backupViewModel,
            )
        }
    }
}