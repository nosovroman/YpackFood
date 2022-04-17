package com.example.ypackfood.activities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ypackfood.components.PictureOneComponent

@Composable
fun HistoryScreen() {
    val scrollState = rememberScrollState()
    Scaffold {
        Column (
            Modifier
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(scrollState)
        ) {
            HistoryCardComponent("1", 1200, "Завершен")
            HistoryCardComponent("2", 1500, "Ждет курьера")
            HistoryCardComponent("3", 2100, "Курьер в пути")
            HistoryCardComponent("4", 850, "Готовится")
        }
    }
}

@Composable
fun HistoryCardComponent(id: String, total: Int, status: String) {
    Column(
        modifier = Modifier
            .padding(top = 50.dp)
    ) {
        Text(text = "Заказ № $id | $status")
        PictureRowComponent()
        Text(text = "28.01.2022, 19:20")
        Text(text = "Сумма: $total ₽")
        Button(onClick = {  }) {
            Text(text = "Повторить заказ")
        }
    }
}

@Composable
fun PictureRowComponent() {
    LazyRow{
        items(count = 10) {
            PictureOneComponent(size = 40.dp)
        }
    }
}