package com.sinxn.mydiary.ui.screens.lockScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinxn.mydiary.R
import com.sinxn.mydiary.ui.theme.MyDiaryTheme
import com.sinxn.mytasks.ui.components.RectangleButton
import showBiometricsAuthentication

@Composable
fun LockScreen(
    onUnlock: () -> Unit
) {
    val context = LocalContext.current

    fun authenticate(function: () -> Unit) {
        showBiometricsAuthentication(
            context,
            onSuccess = function,
            onError = { errString ->
                // Authentication error
                Toast.makeText(context, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }
        )
    }
    MyDiaryTheme {
        Scaffold {
            Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    HorizontalDivider()
                    Text(text = stringResource(R.string.app_name), fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.padding(bottom = 30.dp))
                    Text(text = stringResource(R.string.auth_locked))
                    Spacer(modifier = Modifier.padding(bottom = 10.dp))
                    RectangleButton(onClick = {
                        authenticate(onUnlock)
                    }) {
                        Text( text = stringResource(R.string.auth_locked_button), textAlign = TextAlign.Center)
                    }
                    //TODO Remove this button
//                    RectangleButton(onClick = {
//                        onUnlock()
//                    }) {
//                        Text( text = stringResource(R.string.auth_locked_button), textAlign = TextAlign.Center)
//                    }
                }
            }
        }

    }

    LaunchedEffect(Unit) {
        authenticate(onUnlock)
    }
}