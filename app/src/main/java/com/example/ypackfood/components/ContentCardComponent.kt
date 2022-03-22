package com.example.ypackfood.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContentCardComponent(cardName: String) {
    Row(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth(),
        content = {
            PictureOneComponent()
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = "Мегавкусное блюдо $cardName", fontSize = 16.sp)
                Text(text = "30 см / 540 г", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = "Картошечка, смесь сыров чеддер и пармезан, моцарелла, бекон, соус альфредо, томаты, берем пучок укропу и пицца готова, оцарелла, бекон, соус альфредо, томаты, берем пучок укропу и пицца готова",
                    fontSize = 14.sp
                )
                OutlinedButton(onClick = {  }) {
                    Text(text = "от 369 ₽")
                }
            }
        }
    )
}