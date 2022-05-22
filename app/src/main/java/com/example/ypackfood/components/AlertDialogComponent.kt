package com.example.ypackfood.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AlertDialogComponent(
    title: String,
    body: @Composable () -> Unit,
    onClickConfirm: () -> Unit,
    onClickDismiss: () -> Unit,
    confirmButtonText: String = "Ок",
    dismissButtonText: String = "Назад",
) {
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .padding(20.dp)
            .wrapContentHeight(),
        title = { Text(text = title) },
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
        },
        onDismissRequest = onClickDismiss
    )
}