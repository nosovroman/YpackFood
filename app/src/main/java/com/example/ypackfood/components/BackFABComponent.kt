package com.example.ypackfood.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BackFABComponent(paddingStart: Dp = 10.dp, paddingTop: Dp = 10.dp) {
        FloatingActionButton(
        onClick = { },
        modifier = Modifier.padding(start = paddingStart, top = paddingTop),
        content = { Icon(Icons.Filled.ArrowBack, contentDescription = "Назад") }
    )
}