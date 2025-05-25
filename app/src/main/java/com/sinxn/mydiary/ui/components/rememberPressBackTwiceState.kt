package com.sinxn.mydiary.ui.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

@Composable
fun rememberPressBackTwiceState(
    enabled: Boolean, // True if the "press back twice" behavior should be active
    onExit: () -> Unit,
    message: String = "Press Back Again to exit"
): () -> Unit { // Returns a lambda to be called on back press attempt
    var backPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(backPressedOnce) {
        if (backPressedOnce) {
            delay(2000L)
            backPressedOnce = false
        }
    }

    return {
        if (enabled && !backPressedOnce) {
            backPressedOnce = true
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else {
            onExit()
        }
    }
}