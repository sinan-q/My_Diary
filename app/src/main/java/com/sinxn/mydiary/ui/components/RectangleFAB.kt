package com.sinxn.mydiary.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun RectangleFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = RectangleShape,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        modifier = modifier.imePadding().padding(end = 20.dp, bottom = 20.dp).border(1.dp, MaterialTheme.colorScheme.outline, shape),
        onClick = onClick,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content
    )
}