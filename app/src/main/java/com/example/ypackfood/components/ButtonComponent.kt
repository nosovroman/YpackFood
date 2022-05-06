package com.example.ypackfood.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    shape: CornerBasedShape = RoundedCornerShape(20.dp)
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        content = { Text(text = text) },
        shape = shape
    )
}