package com.sinxn.mytasks.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyTitle(modifier: Modifier = Modifier,title: String) {
    Column(modifier) {
        Spacer(Modifier.padding(top = 20.dp))
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        HorizontalDivider(Modifier.width(80.dp).padding(top = 4.dp, bottom = 15.dp), thickness = 3.dp, color = MaterialTheme.colorScheme.outline)
    }

}