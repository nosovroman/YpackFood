package com.example.ypackfood.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun EmptyContentComponent(message: String) {
    Box (contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = message,
            textAlign = TextAlign.Center
        )
    }
}