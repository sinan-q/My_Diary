package com.sinxn.mydiary.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun RectangleCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    colors: CardColors = CardDefaults.cardColors(containerColor = Color.Transparent),
    shape: androidx.compose.ui.graphics.Shape = RectangleShape,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape)
            .clickable {
                if (onClick != null) {
                    onClick()
                }
            }.then(modifier),
        colors = colors
    ) {
        content()
    }
}