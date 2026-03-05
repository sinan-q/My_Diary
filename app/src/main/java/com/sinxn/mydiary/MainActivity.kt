package com.sinxn.mydiary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.sinxn.mydiary.ui.navigation.NavGraph
import com.sinxn.mydiary.ui.theme.MyDiaryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainAppContent()
        }
    }
}

@Composable
fun MainAppContent() {
    val navController = rememberNavController()

    MyDiaryTheme {
        Surface {
            NavGraph(
                navController = navController,
            )
        }
    }
}