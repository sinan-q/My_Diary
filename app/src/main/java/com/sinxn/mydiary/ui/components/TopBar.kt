package com.sinxn.mydiary.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.sinxn.mydiary.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    navController: NavController,
) {
    var topAppBarExpanded by remember { mutableStateOf(false) }
    val dropdownMenuOptions = listOf(Screen.Settings, Screen.Backup, Screen.About)

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
                dropdownMenuOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name ?: "") },
                        onClick = {
                            topAppBarExpanded = false
                            navController.navigate(option.route)
                        }
                    )
                }
            }
        },
        title = { Text("My Diary", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = MaterialTheme.colorScheme.onSurface) },

    )
}