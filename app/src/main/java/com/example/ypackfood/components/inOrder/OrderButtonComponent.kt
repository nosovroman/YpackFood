package com.example.ypackfood.components.inOrder

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrderButtonComponent(totalCost: Int, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth(),
        onClick = onClick,
        text = {
            if (totalCost != 0)
                Text(text = "Оформить на $totalCost ₽", color = MaterialTheme.colors.onPrimary)
            else
                Text(text = "Меню", color = MaterialTheme.colors.onPrimary)
        },
        backgroundColor = MaterialTheme.colors.primary
    )
}