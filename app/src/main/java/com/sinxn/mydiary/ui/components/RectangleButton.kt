package com.sinxn.mytasks.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun RectangleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = RectangleShape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(),
    content: @Composable() (RowScope.() -> Unit)

) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border =  BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        content = content
    )
}