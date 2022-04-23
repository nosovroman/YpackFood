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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CounterComponent(
    count: Int,
    onIncClick: () -> Unit,
    onDecClick: () -> Unit
) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        CounterButtonComponent(
            count = count,
            isIncrement = false,
            limit = 1,
            onClick = onDecClick
        )

        Text(
            text = "$count шт.",
            fontSize = 16.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .padding(horizontal = 5.dp)
        )

        CounterButtonComponent(
            count = count,
            isIncrement = true,
            limit = 5,
            onClick = onIncClick
        )
    }
}

@Composable
fun CounterButtonComponent(
    count: Int,
    isIncrement: Boolean,
    limit: Int,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .border(width = 1.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
            .size(30.dp),
        enabled = if (isIncrement) count < limit else count > limit,
        onClick = onClick,
        content = {
            if (isIncrement) Icon(imageVector = Icons.Default.Add, contentDescription = "+", tint = MaterialTheme.colors.onBackground)
            else Icon(imageVector = Icons.Default.Remove, contentDescription = "-", tint = MaterialTheme.colors.primary)
        }
    )
}