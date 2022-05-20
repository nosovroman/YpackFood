package com.example.ypackfood.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CounterComponent(
    count: Int,
    unit: String = "",
    lowerLimit: Int = 1,
    upperLimit: Int = 5,
    onIncClick: () -> Unit = {},
    onDecClick: () -> Unit = {},
    incImgVector: ImageVector = Icons.Filled.NavigateNext,
    decImgVector: ImageVector = Icons.Default.Remove,
    incSymbol: String = "+",
    decSymbol: String = "-"
) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        CounterButtonComponent(
            count = count,
            isIncrement = false,
            limit = lowerLimit,
            onClick = onDecClick,
            decImgVector = decImgVector,
            decSymbol = decSymbol
        )

        Text(
            text = "$count" + " $unit".ifBlank { "" },
            fontSize = 16.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .padding(horizontal = 5.dp)
        )

        CounterButtonComponent(
            count = count,
            isIncrement = true,
            limit = upperLimit,
            onClick = onIncClick,
            incImgVector = incImgVector,
            incSymbol = incSymbol
        )
    }
}

@Composable
fun CounterButtonComponent(
    count: Int,
    isIncrement: Boolean,
    limit: Int,
    onClick: () -> Unit,
    incImgVector: ImageVector = Icons.Default.Add,
    decImgVector: ImageVector = Icons.Filled.NavigateNext,
    incSymbol: String = "+",
    decSymbol: String = "-"
) {
    IconButton(
        modifier = Modifier
            .border(width = 1.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
            .size(30.dp),
        enabled = if (isIncrement) count < limit else count > limit,
        onClick = onClick,
        content = {
            if (isIncrement) Icon(imageVector = incImgVector, contentDescription = incSymbol, tint = MaterialTheme.colors.onBackground)
            else Icon(imageVector = decImgVector, contentDescription = decSymbol, tint = MaterialTheme.colors.primary)
        }
    )
}