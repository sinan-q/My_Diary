package com.sinxn.mytasks.ui.screens.backupScreen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.sinxn.mytasks.ui.components.RectangleButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BackupScreen(viewModel: BackupViewModel) {
    val context = LocalContext.current
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val file = uriToFile(it, context)  // Convert Uri to File
            viewModel.importDatabase(context,file)
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
            Toast.makeText(context, state.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    Column {
        RectangleButton(onClick = {
            directoryPickerLauncher.launch("backup.db")
            Toast.makeText(context, "Exported!" , Toast.LENGTH_SHORT).show()
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