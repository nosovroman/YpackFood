package com.example.ypackfood.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun AlertDialogComponent(
    onDismiss: () -> Unit,
    title: String,
    body: @Composable () -> Unit,
    onClickConfirm: () -> Unit,
    onClickDismiss: () -> Unit,
    confirmButtonText: String = "Ок",
    dismissButtonText: String = "Назад",
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = body,
        confirmButton = {
            Button(
                onClick = onClickConfirm ,
                content = { Text(text = confirmButtonText) }
            )
        },
        dismissButton = {
            Button(
                onClick = onClickDismiss ,
                content = { Text(text = dismissButtonText) }
            )
        }
    )
}