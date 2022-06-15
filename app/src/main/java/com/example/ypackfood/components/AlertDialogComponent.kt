package com.example.ypackfood.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
        title = {
            Column {
                Text(text = title, fontWeight = FontWeight.Bold)
//                Spacer(modifier = Modifier.height(20.dp))
//                Divider()
            }
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
        },
        onDismissRequest = onClickDismiss
    )
}