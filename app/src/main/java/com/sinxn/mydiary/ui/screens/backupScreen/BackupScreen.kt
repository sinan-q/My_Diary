package com.sinxn.mydiary.ui.screens.backupScreen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sinxn.mydiary.R
import com.sinxn.mytasks.ui.components.RectangleButton
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    viewModel: BackupViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.importDatabase(context,uri)
        }
    }

    val directoryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        uri?.let {
            viewModel.exportDatabase(context, it)
        }
    }

    LaunchedEffect(true) {
        viewModel.backupState.collectLatest { state ->
            if (state is BackupViewModel.BackupState.Error)
            Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            else if (state is BackupViewModel.BackupState.Completed)
                Toast.makeText(context, "Completed Successfully", Toast.LENGTH_SHORT).show()

        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.backup_title),  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = MaterialTheme.colorScheme.onSurface) }) // Replace with your string resource
        }
    ) {
        Box(modifier = Modifier.padding(it).fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Column {
                RectangleButton(onClick = {
                    directoryPickerLauncher.launch("backup.db")
                }) {
                    Text("Export Database")
                }

                RectangleButton(onClick = {
                    filePickerLauncher.launch("*/*")
                }) {
                    Text("Import Database")
                }
            }
        }
    }

}