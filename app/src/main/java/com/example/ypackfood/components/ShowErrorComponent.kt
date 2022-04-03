package com.example.ypackfood.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ypackfood.viewModels.MainViewModel

@Composable
fun ShowErrorComponent(mvvmViewModel: MainViewModel) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom
    ) {
        SnackBarComponent(
            message = "Ошибка",
            buttonText = "Обновить",
            onButtonClick = {
                mvvmViewModel.getMainContent()
            }
        )
    }
}