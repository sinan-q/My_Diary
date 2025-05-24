package com.sinxn.mydiary.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    onBackup: () -> Unit,
) {
    var topAppBarExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        actions = {
            IconButton(
                onClick = { topAppBarExpanded = true }
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More Options",
                )
            }
            DropdownMenu(
                expanded = topAppBarExpanded,
                onDismissRequest = { topAppBarExpanded = false }
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp).clickable {
                        topAppBarExpanded = false
                        onBackup()
                    },
                    text = "Backup"
                )
            }
        },
        title = { Text("My Diary", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = MaterialTheme.colorScheme.onSurface) },

    )
}