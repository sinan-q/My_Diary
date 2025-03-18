package com.sinxn.mytasks.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlin.math.ceil

@Composable
fun <T> MyGrid(
    modifier: Modifier = Modifier,
    list: List<T>,
    columns: Int,
    content: @Composable RowScope.(T) -> Unit
) {
    val rows = ceil(list.size / columns.toFloat()).toInt()
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        for (x in 0 until rows) {
            Row(Modifier.fillMaxWidth()) {
                for (y in 0 until columns) {
                    val index = x*columns + y
                    if(index >= list.size){
                        break
                    }
                    content(list[index])
                }
            }
        }
    }
}