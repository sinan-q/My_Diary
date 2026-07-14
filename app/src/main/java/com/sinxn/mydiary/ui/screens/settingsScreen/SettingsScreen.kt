package com.sinxn.mydiary.ui.screens.settingsScreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.sinxn.mydiary.R
import com.sinxn.mydiary.ui.navigation.Screen
import com.sinxn.mydiary.ui.screens.lockScreen.LockState
import com.sinxn.mydiary.utils.showBiometricsAuthentication
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isBiometricAuthEnabled = viewModel.isBiometricAuthEnabled.collectAsState()
    val isDefaultTitleEnabled = viewModel.isDefaultTitleEnabled.collectAsState()
    val currentDateFormat by viewModel.dateFormat.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var tempPattern by remember(currentDateFormat) { mutableStateOf(currentDateFormat) }

    val today = remember { LocalDate.now() }

    val isPatternValid by remember(tempPattern) {
        derivedStateOf {
            if (tempPattern.isBlank()) false
            else {
                try {
                    today.format(DateTimeFormatter.ofPattern(tempPattern))
                    true
                } catch (e: Exception) {
                    false
                }
            }
        }
    }

    val dialogPreviewText by remember(tempPattern, isPatternValid) {
        derivedStateOf {
            if (isPatternValid) {
                try {
                    val formatter = DateTimeFormatter.ofPattern(tempPattern, Locale.getDefault())
                    today.format(formatter)
                } catch (e: Exception) {
                    "Invalid Pattern"
                }
            } else {
                "Invalid Pattern"
            }
        }
    }

    val formattedPreview = remember(currentDateFormat) {
        try {
            val formatter = DateTimeFormatter.ofPattern(currentDateFormat, Locale.getDefault())
            today.format(formatter)
        } catch (e: Exception) {
            currentDateFormat
        }
    }

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

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.settings_title),  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = MaterialTheme.colorScheme.onSurface) }) // Replace with your string resource
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.setting_enable_biometric_auth)) // Replace with your string resource
                Switch(
                    checked = isBiometricAuthEnabled.value == LockState.LOCKED,
                    onCheckedChange = { isChecked ->
                        authenticate {
                            viewModel.setBiometricAuthEnabled(isChecked)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.setting_enable_default_title))
                Switch(
                    checked = isDefaultTitleEnabled.value,
                    onCheckedChange = { isChecked ->
                        viewModel.setDefaultTitleEnabled(isChecked)
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        tempPattern = currentDateFormat
                        showDialog = true
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Date Format")
                    Text(
                        text = formattedPreview,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.Backup.route) }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Backup & Restore")
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Navigate to Backup & Restore"
                )
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Date Format") },
                    text = {
                        Column {
                            Text(
                                text = "Enter a custom date format pattern \n(e.g. yyyy-MM-dd EEEE, dd/MM/yyyy EEE).\nNote: Time is not supported",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            OutlinedTextField(
                                value = tempPattern,
                                onValueChange = { tempPattern = it },
                                label = { Text("Date Pattern") },
                                singleLine = true,
                                isError = !isPatternValid,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Preview: $dialogPreviewText",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isPatternValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.setDateFormat(tempPattern)
                                showDialog = false
                            },
                            enabled = isPatternValid
                        ) {
                            Text("Save")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

        }
    }
}

// Example of a reusable SettingItem (optional)
@Composable
fun SettingItem(title: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = title, modifier = Modifier.padding(vertical = 8.dp))
    }
}
