package com.example.ypackfood.components.inOrder

import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun OrderButtonComponent(totalCost: Int) {
    ExtendedFloatingActionButton(
        onClick = {  },
        text = { Text(text = "Оформить на $totalCost ₽", color = MaterialTheme.colors.onPrimary) },
        backgroundColor = MaterialTheme.colors.primary
    )
}