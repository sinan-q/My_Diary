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
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sinxn.mydiary.ui.navigation.NavGraph
import com.sinxn.mydiary.ui.screens.diaryScreen.DiaryViewModel
import com.sinxn.mydiary.ui.theme.MyDiaryTheme
import com.sinxn.mytasks.ui.screens.backupScreen.BackupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val backupViewModel: BackupViewModel by viewModels()
    private val diaryViewModel: DiaryViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            MyDiaryTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets.safeContent,
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
    }
}