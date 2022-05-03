package com.example.ypackfood.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ypackfood.viewModels.MainViewModel

@Composable
fun ShowErrorComponent(
    message: String? = null,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom
    ) {
        SnackBarComponent(
            message = if (message.isNullOrBlank()) "Ошибка загрузки данных" else message,
            buttonText = "Обновить",
            onButtonClick = onButtonClick
        )
    }
}