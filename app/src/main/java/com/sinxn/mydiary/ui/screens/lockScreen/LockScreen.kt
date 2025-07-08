package com.sinxn.mydiary.ui.screens.lockScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sinxn.mydiary.R
import com.sinxn.mytasks.ui.components.RectangleButton
import showBiometricsAuthentication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockScreen(
    viewModel: LockViewModel = hiltViewModel(),
    onUnlock: () -> Unit,
) {
    val context = LocalContext.current

    val isBiometricAuthEnabled = viewModel.isBiometricAuthEnabled.collectAsState()

    fun authenticate(function: () -> Unit) {
        showBiometricsAuthentication(
            context,
            onSuccess = function,
            onError = { errString ->
                Toast.makeText(context, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }
        )
    }

    LaunchedEffect(isBiometricAuthEnabled.value) {
        if (isBiometricAuthEnabled.value == LockState.UNLOCKED) {
            onUnlock()
        } else if (isBiometricAuthEnabled.value == LockState.LOCKED) {
            authenticate(onUnlock)
        }
    }


    Scaffold {
        Box(modifier = Modifier.padding(it)
            .fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalDivider()
                Text(text = stringResource(R.string.app_name), fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.padding(bottom = 30.dp))
                if (isBiometricAuthEnabled.value == LockState.LOCKED) {
                    Text(text = stringResource(R.string.auth_locked))
                    Spacer(modifier = Modifier.padding(bottom = 10.dp))
                    RectangleButton(onClick = {
                        authenticate(onUnlock)
                    }) {
                        Text( text = stringResource(R.string.auth_locked_button), textAlign = TextAlign.Center)
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp), // Adjust size as needed
                        color = MaterialTheme.colorScheme.primary // Optional: customize color
                    )
                    Spacer(modifier = Modifier.padding(bottom = 16.dp))
                }
            }
        }
    }

}