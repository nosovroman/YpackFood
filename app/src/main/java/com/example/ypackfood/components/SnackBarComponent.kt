package com.example.ypackfood.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SnackBarComponent(message: String, buttonText: String, onButtonClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.Red,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(5.dp),
        content = {
            Text(
                text = message,
                color = MaterialTheme.colors.onBackground,
            )
            TextButton(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Color.Blue,
                        shape = RoundedCornerShape(10.dp),
                    ),
                onClick = onButtonClick,
                content = {
                    Text(
                        text = buttonText,
                        color = MaterialTheme.colors.onBackground,
                    )
                }
            )
        }
    )
}